package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.NotificationDataSource
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto
import com.sopt.clody.data.repository.NotificationRepository
import com.sopt.clody.data.remote.util.handleApiResponse
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {
    override suspend fun getNotificationInfo(): Result<NotificationInfoResponseDto> {
        return runCatching {
            val response = notificationDataSource.getNotificationInfo()
            response.handleApiResponse().getOrThrow()
        }
    }

    override suspend fun sendNotification(sendNotificationRequestDto: SendNotificationRequestDto): Result<SendNotificationResponseDto> {
        return runCatching {
            val response = notificationDataSource.sendNotification(sendNotificationRequestDto)
            response.handleApiResponse().getOrThrow()
        }
    }
}
