package com.sopt.clody.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseReplyDiaryDto(
    val content: String?,
    val nickname: String,
    val month: Int,
    val date: Int
)

