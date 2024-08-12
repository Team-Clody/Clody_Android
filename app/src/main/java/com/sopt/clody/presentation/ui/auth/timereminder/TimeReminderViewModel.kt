package com.sopt.clody.presentation.ui.auth.timereminder

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.request.RequestSendNotificationDto
import com.sopt.clody.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeReminderViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _timeReminderState = MutableStateFlow<TimeReminderState>(TimeReminderState.Idle)
    val timeReminderState: StateFlow<TimeReminderState> = _timeReminderState

    var selectedTime by mutableStateOf("")

    fun sendNotification(context: Context, isPermissionGranted: Boolean) {
        viewModelScope.launch {
            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _timeReminderState.value = TimeReminderState.Failure("FCM 못가져옴")
                return@launch
            }

            val requestDto = RequestSendNotificationDto(
                isDiaryAlarm = isPermissionGranted,
                isReplyAlarm = isPermissionGranted,
                time = selectedTime,
                fcmToken = fcmToken
            )

            _timeReminderState.value = TimeReminderState.Loading
            notificationRepository.sendNotification(requestDto).fold(
                onSuccess = {
                    _timeReminderState.value = TimeReminderState.Success("알림 전송 성공")
                },
                onFailure = {
                    _timeReminderState.value = TimeReminderState.Failure(it.message ?: "알림 전송 실패")
                }
            )
        }
    }

    private fun getTokenFromPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("fcm_token", null)
    }

    fun setSelectedTime(amPm: String, hour: String, minute: String) {
        selectedTime = formatTime(amPm, hour, minute)
    }

    fun setFixedTime(hour: String, minute: String) {
        selectedTime = String.format("%02d:%02d", hour.toInt(), minute.toInt())
    }

    private fun formatTime(amPm: String, hour: String, minute: String): String {
        val hourInt = if (amPm == "오후" && hour.toInt() != 12) {
            hour.toInt() + 12
        } else if (amPm == "오전" && hour.toInt() == 12) {
            0
        } else {
            hour.toInt()
        }
        return String.format("%02d:%02d", hourInt, minute.toInt())
    }
}
