package com.sopt.clody.data.remote.util

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeApiCall(
    errorMessageProvider: ErrorMessageProvider,
    action: suspend () -> ApiResponse<T>,
): Result<T> {
    return try {
        val response = action()
        response.data?.let { Result.success(it) }
            ?: Result.failure(ApiError(errorMessageProvider.getTemporaryError()))
    } catch (exception: Throwable) {
        if (exception is CancellationException) throw exception

        val error = when (exception) {
            is IOException -> ApiError(errorMessageProvider.getNetworkError())
            else -> ApiError(errorMessageProvider.getTemporaryError())
        }
        Result.failure(error)
    }
}
