package com.sopt.clody.presentation.utils.base

sealed interface UiState<out T> {
    data object Empty : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Failure(val msg: String) : UiState<Nothing>

    fun toUiStateModel() = UiStateModel(
        isEmpty = this is Empty,
        isLoading = this is Loading,
        isSuccess = this is Success<*>,
        isFailure = this is Failure
    )
}

data class UiStateModel(
    val isEmpty: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isFailure: Boolean = false
)
