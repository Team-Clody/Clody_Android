package com.sopt.clody.presentation.ui.home.screen

sealed class CalendarState<out T> {
    object Idle : CalendarState<Nothing>()
    object Loading : CalendarState<Nothing>()
    data class Success<out T>(val data: T) : CalendarState<T>()
    data class Error(val message: String) : CalendarState<Nothing>()
}
