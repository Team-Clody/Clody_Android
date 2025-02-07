package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.NotificationService
import com.sopt.clody.data.remote.datasource.NotificationDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.ResponseNotificationInfoDto
import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val notificationService: NotificationService
) : NotificationDataSource {
    override suspend fun getNotificationInfo(): ApiResponse<ResponseNotificationInfoDto> {
        return notificationService.getNotificationInfo()
    }

    override suspend fun sendNotification(sendNotificationRequestDto: SendNotificationRequestDto): ApiResponse<ResponseSendNotificationDto> {
        return notificationService.sendNotification(sendNotificationRequestDto)
    }
}
