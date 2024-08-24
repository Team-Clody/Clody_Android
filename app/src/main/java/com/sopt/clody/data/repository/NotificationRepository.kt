package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.request.RequestSendNotificationDto
import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto

interface NotificationRepository {
    suspend fun sendNotification(requestSendNotificationDto: RequestSendNotificationDto): Result<ResponseSendNotificationDto>
}
