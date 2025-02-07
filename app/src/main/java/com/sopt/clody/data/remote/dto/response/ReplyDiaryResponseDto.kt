package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ReplyDiaryResponseDto(
    val content: String?,
    val nickname: String,
    val month: Int,
    val date: Int
)

