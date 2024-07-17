package com.sopt.clody.data.remote.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken =
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MjExMjI1OTIsImV4cCI6MTcyMTEyMzc5MiwidHlwZSI6ImFjY2VzcyIsInVzZXJJZCI6MTh9.0RD-45Ktb37wf6Qx7QNuH7OdXGi9Vt5XWkn1LHZ6nFg"

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", accessToken)
            .build()

        Log.d("TokenInterceptor", "Request URL: ${newRequest.url}")
        Log.d("TokenInterceptor", "Authorization: ${newRequest.header("Authorization")}")

        return chain.proceed(newRequest)
    }

    companion object {
        const val TOKEN = "Token"
    }
}
