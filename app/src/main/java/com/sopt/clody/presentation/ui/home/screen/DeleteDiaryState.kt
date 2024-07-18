package com.sopt.clody.presentation.ui.home.screen

sealed class DeleteDiaryState {
    object Idle : DeleteDiaryState()
    object Loading : DeleteDiaryState()
    data class Success(val message: String) : DeleteDiaryState()
    data class Failure(val errorMessage: String) : DeleteDiaryState()
}
