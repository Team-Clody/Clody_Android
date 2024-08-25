package com.sopt.clody.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseWriteDiaryDto(
    val createdAt: String,
    val replyType: String
)
