package com.sopt.clody.data.remote.util

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.repository.TokenReissueRepository
import com.sopt.clody.presentation.ui.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider

class AuthInterceptor @Inject constructor(
    private val tokenReissueRepositoryProvider: Provider<TokenReissueRepository>,
    private val tokenDataStore: TokenDataStore,
    @ApplicationContext private val context: Context
) : Interceptor {

    private val mutex = Mutex()
    private var tokenRefreshJob: Deferred<Boolean>? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        return if (shouldAddAuthorization(url)) {
            val response = proceedWithAuthorization(chain, originalRequest)
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

    private fun addAuthorizationHeader(request: Request): Request {
        return request.newBuilder()
            .addHeader(AUTHORIZATION, "$BEARER ${tokenDataStore.accessToken}")
            .build()
    }

    private fun proceedWithAuthorization(chain: Interceptor.Chain, request: Request): Response {
        val authRequest = addAuthorizationHeader(request)
        return chain.proceed(authRequest)
    }

    private fun handleTokenExpiration(
        chain: Interceptor.Chain,
        originalRequest: Request
    ): Response {
        return runBlocking {
            mutex.withLock {
                if (tokenRefreshJob?.isCompleted != false) { // 실패했거나 완료된 경우
                    tokenRefreshJob = async {
                        tryReissueToken()
                    }
                }
            }

            val tokenRefreshed = tokenRefreshJob?.await() ?: false

            if (tokenRefreshed) {
                proceedWithAuthorization(chain, originalRequest) // 새 토큰으로 요청 재시도
            } else {
                clearUserInfoAndNavigateToLogin()
                throw IOException("Token expired and reissue failed")
            }
        }
    }

    private fun tryReissueToken(): Boolean {
        val reissueTokenRepository = tokenReissueRepositoryProvider.get()
        return try {
            runBlocking {
                reissueTokenRepository.getReissueToken(tokenDataStore.refreshToken).onSuccess { data ->
                    if (data.accessToken.isEmpty() || data.refreshToken.isEmpty()) {
                        // 빈 토큰 데이터가 반환된 경우
                        Timber.e("Token reissue returned empty tokens")
                        clearUserInfoAndNavigateToLogin()
                    } else {
                        // 정상적인 토큰 데이터가 반환된 경우
                        updateTokens(data.accessToken, data.refreshToken)
                    }
                }.onFailure { throwable ->
                    Timber.e("Token reissue failed: ${throwable.message}")
                    clearUserInfoAndNavigateToLogin()
                }
            }
            true
        } catch (t: Throwable) {
            Timber.e("Unexpected error during token reissue: ${t.message}")
            clearUserInfoAndNavigateToLogin()
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

    private fun clearUserInfoAndNavigateToLogin() {
        tokenDataStore.clearInfo()
        Handler(Looper.getMainLooper()).post {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("NAVIGATE_TO_LOGIN", true)
            }
            context.startActivity(intent)
        }
    }

    companion object {
        private const val TOKEN_EXPIRED = 401
        private const val BEARER = "Bearer"
        private const val AUTHORIZATION = "Authorization"
    }
}
