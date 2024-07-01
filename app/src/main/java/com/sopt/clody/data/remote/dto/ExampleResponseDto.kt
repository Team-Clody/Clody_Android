package com.sopt.clody.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExampleResponseDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String
)
