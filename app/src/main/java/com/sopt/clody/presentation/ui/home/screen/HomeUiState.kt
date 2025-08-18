package com.sopt.clody.presentation.ui.home.screen

sealed class HomeUiState<out T> {
    data object Idle : HomeUiState<Nothing>()
    data object Loading : HomeUiState<Nothing>()
    data class Success<out T>(val data: T) : HomeUiState<T>()
    data class Error(val message: String) : HomeUiState<Nothing>()
}
