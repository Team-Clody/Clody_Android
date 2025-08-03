package com.sopt.clody.data.remote.util

data class ApiError(
    override val message: String,
) : Exception()
