package com.sopt.clody.data.remote.interceptor

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.jakewharton.processphoenix.ProcessPhoenix
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.repository.ReissueTokenRepository
import com.sopt.clody.presentation.ui.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val reissueTokenRepositoryProvider: Provider<ReissueTokenRepository>,
    private val tokenDataStore: TokenDataStore,
    @ApplicationContext private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        return if (shouldAddAuthorization(url)) {
            val authRequest = addAuthorization(originalRequest)
            var response = chain.proceed(authRequest)
            if (response.code == TOKEN_EXPIRED) {
                response.close()
                return handleTokenExpiration(chain, originalRequest)
            }
            response
        } else {
            chain.proceed(originalRequest)
        }
    }

    private fun shouldAddAuthorization(url: String): Boolean {
        return !url.contains("api/v1/auth/signin") &&
                !url.contains("api/v1/auth/signup") &&
                !url.contains("api/v1/auth/reissue")
    }

    private fun addAuthorization(request: Request): Request {
        return if (tokenDataStore.accessToken.isNotBlank()) {
            request.newBuilder().addAuthorizationHeader().build()
        } else {
            request
        }
    }

    private fun Request.Builder.addAuthorizationHeader() =
        this.addHeader(AUTHORIZATION, "$BEARER ${tokenDataStore.accessToken}")

    private fun handleTokenExpiration(
        chain: Interceptor.Chain,
        originalRequest: Request
    ): Response {
        return if (tryReissueToken()) {
            val newRequest = originalRequest.newBuilder().addAuthorizationHeader().build()
            chain.proceed(newRequest)
        } else {
            clearUserInfoAndRestart()
            throw IOException("Token expired and reissue failed")
        }
    }

    private fun tryReissueToken(): Boolean {
        val reissueTokenRepository = reissueTokenRepositoryProvider.get()
        return try {
            runBlocking {
                reissueTokenRepository.getReissueToken(tokenDataStore.refreshToken).onSuccess { data ->
                    updateTokens(data.accessToken, data.refreshToken)
                }
            }
            true
        } catch (t: Throwable) {
            Timber.d(t.message)
            false
        }
    }

    private fun updateTokens(newAccessToken: String, newRefreshToken: String) {
        Timber.d("NEW ACCESS TOKEN : $newAccessToken")
        Timber.d("NEW REFRESH TOKEN : $newRefreshToken")
        tokenDataStore.apply {
            accessToken = newAccessToken
            refreshToken = newRefreshToken
        }
    }

    private fun clearUserInfoAndRestart() {
        tokenDataStore.clearInfo()
        Handler(Looper.getMainLooper()).post {
            ProcessPhoenix.triggerRebirth(context, Intent(context, MainActivity::class.java))
        }
    }

    companion object {
        private const val TOKEN_EXPIRED = 401
        private const val BEARER = "Bearer"
        private const val AUTHORIZATION = "Authorization"
    }
}
