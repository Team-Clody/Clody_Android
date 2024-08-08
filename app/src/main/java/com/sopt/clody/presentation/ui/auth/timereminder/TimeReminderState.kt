package com.sopt.clody.presentation.ui.auth.timereminder

sealed class TimeReminderState {
    data object Idle : TimeReminderState()
    data object Loading : TimeReminderState()
    data class Success(val message: String) : TimeReminderState()
    data class Failure(val error: String) : TimeReminderState()
}
