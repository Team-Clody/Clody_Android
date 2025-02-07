package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class DiaryTimeResponseDto(
    val HH: Int,
    val mm: Int,
    val ss: Int,
    val isFirst: Boolean,
)
