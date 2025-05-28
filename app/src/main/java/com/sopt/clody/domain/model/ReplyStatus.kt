package com.sopt.clody.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ReplyStatus {
    UNREADY, READY_READ, READY_NOT_READ
}
