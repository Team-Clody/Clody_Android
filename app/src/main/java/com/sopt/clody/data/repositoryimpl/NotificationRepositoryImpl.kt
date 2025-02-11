package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.NotificationDataSource
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto
import com.sopt.clody.domain.repository.NotificationRepository
import com.sopt.clody.data.remote.util.handleApiResponse
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {
    override suspend fun getNotificationInfo(): Result<NotificationInfoResponseDto> =
        runCatching {
            notificationDataSource.getNotificationInfo().handleApiResponse().getOrThrow()
        }

    override suspend fun sendNotification(sendNotificationRequestDto: SendNotificationRequestDto): Result<SendNotificationResponseDto> =
        runCatching {
            notificationDataSource.sendNotification(sendNotificationRequestDto).handleApiResponse().getOrThrow()
        }
}
