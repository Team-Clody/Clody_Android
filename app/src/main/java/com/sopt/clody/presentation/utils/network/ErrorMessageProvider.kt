package com.sopt.clody.presentation.utils.network

import com.sopt.clody.data.remote.util.ApiError

interface ErrorMessageProvider {
    fun getTemporaryError(): String
    fun getNetworkError(): String
    fun getServerError(): String
    fun getFetchTempDiaryFailedError(): String
    fun getUnknownError(): String
    fun getLoginFailedError(): String
    fun getSignupFailedError(): String
    fun getGoogleIdTokenMissingError(): String
    fun getNetworkCheckError(): String
    fun getApiError(apiError: ApiError): String
}
