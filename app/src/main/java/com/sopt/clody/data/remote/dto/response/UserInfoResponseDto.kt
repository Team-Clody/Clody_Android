package com.sopt.clody.data.remote.dto.response

import com.sopt.clody.data.datastore.OAuthProvider
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseDto(
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("platform") val platform: OAuthProvider?,
)
