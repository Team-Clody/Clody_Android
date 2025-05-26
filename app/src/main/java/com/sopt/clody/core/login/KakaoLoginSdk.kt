package com.sopt.clody.core.login

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class KakaoLoginSdk @Inject constructor() : LoginSdk {
    override suspend fun login(context: Context): Result<LoginAccessToken> = runCatching {
        suspendCancellableCoroutine { continuation ->
            val callback: (OAuthToken?, Throwable?) -> Unit = callback@{ token, throwable ->
                if (!continuation.isActive) {
                    return@callback
                }

                when {
                    throwable != null -> {
                        if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
                            continuation.resumeWithException(LoginException.CancelException(throwable.message))
                            return@callback
                        }

                        continuation.resumeWithException(LoginException.AuthException(throwable.message))
                    }

                    token != null -> continuation.resume(KakaoAccessToken(token.accessToken))
                }
            }

            val userApiClient = UserApiClient.instance

            if (userApiClient.isKakaoTalkLoginAvailable(context)) {
                userApiClient.loginWithKakaoTalk(
                    context,
                    callback = callback@{ oAuthToken, throwable ->
                        // 카카오톡이 설치되어 있으나 로그인되어 있지 않은 경우 대응
                        if (throwable is AuthError && throwable.statusCode == 302) {
                            userApiClient.loginWithKakaoAccount(context, callback = callback)
                            return@callback
                        }

                        callback(oAuthToken, throwable)
                    },
                )
            } else {
                // 카카오톡 웹 로그인
                userApiClient.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        suspendCancellableCoroutine { continuation ->
            val userApiClient = UserApiClient.instance

            userApiClient.logout { throwable ->
                when {
                    throwable != null -> continuation.resumeWithException(LoginException.AuthException(throwable.message))
                    else -> continuation.resume(Unit)
                }
            }
        }
    }

    override suspend fun unlink(): Result<Unit> = runCatching {
        suspendCancellableCoroutine { continuation ->
            val userApiClient = UserApiClient.instance

            userApiClient.unlink { throwable ->
                when {
                    throwable != null -> continuation.resumeWithException(LoginException.AuthException(throwable.message))
                    else -> continuation.resume(Unit)
                }
            }
        }
    }
}
