package com.sopt.clody.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header(TOKEN, "")
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val TOKEN = "Token"
    }
}
