package com.sopt.clody.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiaryTimeResponseDto(
    @SerialName("HH") val HH: Int,
    @SerialName("mm") val mm: Int,
    @SerialName("ss") val ss: Int,
    @SerialName("date") val date: String,
    @SerialName("isFirst") val isFirst: Boolean,
    @SerialName("isFromAd") val isFromAd: Boolean,
)
