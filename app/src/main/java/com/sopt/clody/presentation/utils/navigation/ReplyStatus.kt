package com.sopt.clody.presentation.utils.navigation

import kotlinx.serialization.Serializable

@Serializable
enum class ReplyStatus {
    UNREADY, READY_READ, READY_NOT_READ
}
