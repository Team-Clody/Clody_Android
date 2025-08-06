package com.sopt.clody.presentation.utils.network

import android.content.Context
import com.sopt.clody.R
import com.sopt.clody.data.remote.util.ApiError
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ErrorMessageProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ErrorMessageProvider {

    override fun getTemporaryError(): String {
        return context.getString(R.string.error_temporary)
    }

    override fun getNetworkError(): String {
        return context.getString(R.string.error_network)
    }

    override fun getServerError(): String {
        return context.getString(R.string.error_server)
    }

    override fun getFetchTempDiaryFailedError(): String {
        return context.getString(R.string.error_fetch_temp_diary_failed)
    }

    override fun getUnknownError(): String {
        return context.getString(R.string.error_unknown)
    }

    override fun getLoginFailedError(): String {
        return context.getString(R.string.error_login_failed)
    }

    override fun getSignupFailedError(): String {
        return context.getString(R.string.error_signup_failed)
    }

    override fun getGoogleIdTokenMissingError(): String {
        return context.getString(R.string.error_google_id_token_missing)
    }

    override fun getNetworkCheckError(): String {
        return context.getString(R.string.error_network_check)
    }

    override fun getApiError(apiError: ApiError): String {
        return apiError.message
    }
}
