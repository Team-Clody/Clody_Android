package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.NotificationService
import com.sopt.clody.data.remote.datasource.NotificationDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.RequestSendNotificationDto
import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val notificationService: NotificationService
) : NotificationDataSource {
    override suspend fun sendNotification(requestSendNotificationDto: RequestSendNotificationDto): ApiResponse<ResponseSendNotificationDto> {
        return notificationService.sendNotification(requestSendNotificationDto)
    }
}
