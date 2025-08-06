package com.sopt.clody.core.login

sealed class LoginException(override val message: String?) : Exception(message) {
    class CancelException(message: String?) : LoginException(message)
    class AuthException(message: String?) : LoginException(message)
}
