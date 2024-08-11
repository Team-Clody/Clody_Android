package com.sopt.clody.presentation.ui.diarylist.screen

sealed class DiaryDeleteState {
    data object Idle : DiaryDeleteState()
    data object Loading : DiaryDeleteState()
    data object Success : DiaryDeleteState()
    data class Failure(val errorMessage: String) : DiaryDeleteState()
}
