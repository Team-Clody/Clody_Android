package com.sopt.clody.presentation.ui.type

import kotlinx.serialization.Serializable

@Serializable
enum class ReplyStatus {
    UNREADY, READY_READ, READY_NOT_READ, HAS_DRAFT, INVALID_DRAFT;

    val isUnreadOrNotRead: Boolean
        get() = this == UNREADY || this == READY_NOT_READ
}
