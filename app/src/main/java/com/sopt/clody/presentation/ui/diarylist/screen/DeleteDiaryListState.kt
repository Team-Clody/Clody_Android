package com.sopt.clody.presentation.ui.diarylist.screen

sealed class DeleteDiaryListState {
    object Idle : DeleteDiaryListState()
    object Loading : DeleteDiaryListState()
    object Success : DeleteDiaryListState()
    data class Failure(val errorMessage: String) : DeleteDiaryListState()
}
