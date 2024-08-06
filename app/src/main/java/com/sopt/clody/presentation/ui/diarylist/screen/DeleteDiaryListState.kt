package com.sopt.clody.presentation.ui.diarylist.screen

sealed class DeleteDiaryListState {
    data object Idle : DeleteDiaryListState()
    data object Loading : DeleteDiaryListState()
    data object Success : DeleteDiaryListState()
    data class Failure(val errorMessage: String) : DeleteDiaryListState()
}
