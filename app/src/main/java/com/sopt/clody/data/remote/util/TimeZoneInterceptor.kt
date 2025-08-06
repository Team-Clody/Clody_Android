package com.sopt.clody.data.remote.util

import okhttp3.Interceptor
import okhttp3.Response
import java.time.ZoneId
import javax.inject.Inject

class TimeZoneInterceptor @Inject constructor() : Interceptor {

    private val userTimeZone = ZoneId.systemDefault().id

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        return if (shouldAddTimeZoneHeader(url)) {
            proceedWithTimeZoneHeader(chain, originalRequest)
        } else {
            chain.proceed(originalRequest)
        }
    }

    private fun shouldAddTimeZoneHeader(url: String) =
        url.contains("api/v1/diary") ||
            url.contains("api/v1/diary/time") ||
            url.contains("api/v1/calendar") ||
            url.contains("api/v1/calendar/list") ||
            url.contains("api/v1/reply") ||
            url.contains("api/v1/reply/ad/start") ||
            url.contains("api/v1/reply/ad/end") ||
            url.contains("api/v1/draft")

    private fun proceedWithTimeZoneHeader(chain: Interceptor.Chain, request: okhttp3.Request): Response =
        chain.proceed(request.newBuilder().addHeader(TIME_ZONE, userTimeZone).build())

    companion object {
        private const val TIME_ZONE = "Time-Zone"
    }
}
