package com.sopt.clody.presentation.ui.replyloading.screen

import com.sopt.clody.presentation.ui.replydiary.ReplyDiaryState

sealed class ReplyLoadingState {
    data object Idle : ReplyLoadingState()
    data object Loading : ReplyLoadingState()
    data class Success(val HH: Int, val MM: Int, val SS: Int) : ReplyLoadingState()

    data class Failure(val error: String) : ReplyLoadingState()
}
