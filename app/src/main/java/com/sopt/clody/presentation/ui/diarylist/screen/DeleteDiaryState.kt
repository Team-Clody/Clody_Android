package com.sopt.clody.presentation.ui.diarylist.screen

sealed class DeleteDiaryState {
    data object Idle : DeleteDiaryState()
    data object Loading : DeleteDiaryState()
    data object Success : DeleteDiaryState()
    data class Failure(val errorMessage: String) : DeleteDiaryState()
}
