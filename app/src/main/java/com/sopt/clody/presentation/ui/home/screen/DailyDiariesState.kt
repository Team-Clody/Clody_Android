package com.sopt.clody.presentation.ui.home.screen

sealed class DailyDiariesState<out T> {
    object Idle : DailyDiariesState<Nothing>()
    object Loading : DailyDiariesState<Nothing>()
    data class Success<out T>(val data: T) : DailyDiariesState<T>()
    data class Error(val message: String) : DailyDiariesState<Nothing>()
}
