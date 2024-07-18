package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDiaryTimeDto(
    val HH: Int,
    val MM: Int,
    val SS: Int
)
