package com.sopt.clody.presentation.ui.replyloading.screen

import java.time.LocalDateTime

sealed class ReplyLoadingState {
    data object Idle : ReplyLoadingState()
    data object Loading : ReplyLoadingState()
    data class Success(val targetDateTime: LocalDateTime) : ReplyLoadingState()
    data class Failure(val error: String) : ReplyLoadingState()
}
