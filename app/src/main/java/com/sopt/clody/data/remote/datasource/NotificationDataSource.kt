package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto

interface NotificationDataSource {

    suspend fun getNotificationInfo(): ApiResponse<NotificationInfoResponseDto>
    suspend fun sendNotification(sendNotificationRequestDto: SendNotificationRequestDto): ApiResponse<SendNotificationResponseDto>
}
