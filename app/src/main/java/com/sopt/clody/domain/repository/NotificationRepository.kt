package com.sopt.clody.domain.repository

import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto

interface NotificationRepository {
    suspend fun getNotificationInfo(): Result<NotificationInfoResponseDto>
    suspend fun sendNotification(sendNotificationRequestDto: SendNotificationRequestDto): Result<SendNotificationResponseDto>
}
