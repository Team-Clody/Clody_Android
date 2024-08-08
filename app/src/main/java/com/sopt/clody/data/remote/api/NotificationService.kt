package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.RequestSendNotificationDto
import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationService {
    @POST("api/v1/alarm")
    suspend fun sendNotification(
        @Body requestSendNotificationDto: RequestSendNotificationDto
    ): ApiResponse<ResponseSendNotificationDto>
}
