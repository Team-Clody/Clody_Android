package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.ResponseNotificationInfoDto
import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationService {
    @GET("api/v1/alarm")
    suspend fun getNotificationInfo(): ApiResponse<ResponseNotificationInfoDto>

    @POST("api/v1/alarm")
    suspend fun sendNotification(
        @Body sendNotificationRequestDto: SendNotificationRequestDto
    ): ApiResponse<ResponseSendNotificationDto>
}
