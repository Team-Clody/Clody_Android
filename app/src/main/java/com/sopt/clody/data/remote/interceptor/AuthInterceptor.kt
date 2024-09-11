package com.sopt.clody.data.remote.interceptor

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.repository.ReissueTokenRepository
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
    private val reissueTokenRepositoryProvider: Provider<ReissueTokenRepository>,
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
        return !url.contains(AUTH_SIGNIN_URL) &&
                !url.contains(AUTH_SIGNUP_URL) &&
                !url.contains(AUTH_REISSUE_URL)
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
                // 토큰 재발급 중인지 확인하고 이미 진행 중이면 결과를 기달리게 해야됨
                if (tokenRefreshJob == null || tokenRefreshJob?.isCompleted == true) {
                    tokenRefreshJob = async {
                        tryReissueToken()
                    }
                }
            }

            val tokenRefreshed = tokenRefreshJob?.await() ?: false

            if (tokenRefreshed) {
                proceedWithAuthorization(chain, originalRequest) // 새 토큰으로 요청 재시도
            } else {
                // 토큰 재발급 실패 시 로그인으로
                clearUserInfoAndNavigateToLogin()
                throw IOException("Token expired and reissue failed")
            }
        }
    }

    private fun tryReissueToken(): Boolean {
        val reissueTokenRepository = reissueTokenRepositoryProvider.get()
        return try {
            runBlocking {
                reissueTokenRepository.getReissueToken(tokenDataStore.refreshToken).onSuccess { data ->
                    updateTokens(data.accessToken, data.refreshToken)
                }.onFailure { error ->
                    if (error.message?.contains(REFRESH_TOKEN_EXPIRED.toString()) == true) {
                        clearUserInfoAndNavigateToLogin()
                    }
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
        private const val REFRESH_TOKEN_EXPIRED = 500
        private const val BEARER = "Bearer"
        private const val AUTHORIZATION = "Authorization"
        private const val AUTH_SIGNIN_URL = "api/v1/auth/signin"
        private const val AUTH_SIGNUP_URL = "api/v1/auth/signup"
        private const val AUTH_REISSUE_URL = "api/v1/auth/reissue"
    }
}
