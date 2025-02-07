package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWriteDiaryDto(
    val createdAt: String,
    val replyType: String
)
