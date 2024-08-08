package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.NotificationDataSource
import com.sopt.clody.data.remote.dto.request.RequestSendNotificationDto
import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto
import com.sopt.clody.data.repository.NotificationRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource
) : NotificationRepository {
    override suspend fun sendNotification(requestSendNotificationDto: RequestSendNotificationDto): Result<ResponseSendNotificationDto> {
        return runCatching {
            val response = notificationDataSource.sendNotification(requestSendNotificationDto)
            response.handleApiResponse().getOrThrow()
        }
    }
}
