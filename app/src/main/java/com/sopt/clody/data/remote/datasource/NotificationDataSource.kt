package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.RequestSendNotificationDto
import com.sopt.clody.data.remote.dto.response.ResponseNotificationInfoDto
import com.sopt.clody.data.remote.dto.response.ResponseSendNotificationDto

interface NotificationDataSource {

    suspend fun getNotificationInfo(): ApiResponse<ResponseNotificationInfoDto>
    suspend fun sendNotification(requestSendNotificationDto: RequestSendNotificationDto): ApiResponse<ResponseSendNotificationDto>
}
