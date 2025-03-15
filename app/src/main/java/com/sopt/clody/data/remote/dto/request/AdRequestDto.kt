package com.sopt.clody.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdRequestDto(
    @SerialName("year") val year: Int,
    @SerialName("month") val month: Int,
    @SerialName("date") val date: Int,
)
