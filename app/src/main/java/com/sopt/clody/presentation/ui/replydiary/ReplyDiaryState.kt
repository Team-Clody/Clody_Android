package com.sopt.clody.presentation.ui.replydiary

sealed class ReplyDiaryState {
    object Idle : ReplyDiaryState()
    object Loading : ReplyDiaryState()
    data class Success(val content: String, val nickname: String, val month: Int, val date: Int) : ReplyDiaryState()
    data class Failure(val error: String) : ReplyDiaryState()
}
