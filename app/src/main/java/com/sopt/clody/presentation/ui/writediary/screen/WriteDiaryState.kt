package com.sopt.clody.presentation.ui.writediary.screen

import kotlinx.serialization.Serializable

@Serializable
sealed class WriteDiaryState {
    data object Idle : WriteDiaryState()
    data object Loading : WriteDiaryState()
    data class Success(val createdAt: String) : WriteDiaryState()
    data class Failure(val error: String) : WriteDiaryState()
    data object NoReply : WriteDiaryState()
}
