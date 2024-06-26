package com.sopt.clody.utils.base

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val code: Int,
    val data: T? = null,
    val message: String,
)
