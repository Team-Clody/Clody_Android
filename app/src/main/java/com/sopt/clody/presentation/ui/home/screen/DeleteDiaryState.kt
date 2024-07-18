package com.sopt.clody.presentation.ui.home.screen

sealed class DeleteDiaryState {
    object Idle : DeleteDiaryState()
    object Loading : DeleteDiaryState()
    object Success : DeleteDiaryState()
    data class Failure(val errorMessage: String) : DeleteDiaryState()
}
