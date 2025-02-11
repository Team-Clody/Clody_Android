package com.sopt.clody.presentation.ui.auth.timereminder

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.domain.repository.NotificationRepository
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import com.sopt.clody.data.remote.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeReminderViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _timeReminderState = MutableStateFlow<TimeReminderState>(TimeReminderState.Idle)
    val timeReminderState: StateFlow<TimeReminderState> = _timeReminderState

    var selectedTime by mutableStateOf("21:30")
        private set

    fun sendNotification(context: Context, isPermissionGranted: Boolean) {
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _timeReminderState.value = TimeReminderState.Failure(FAILURE_NETWORK_MESSAGE)
                return@launch
            }

            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _timeReminderState.value = TimeReminderState.Failure("FCM 못가져옴")
                return@launch
            }

            val requestDto = SendNotificationRequestDto(
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
                onFailure = { error ->
                    val errorMessage = if (error.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        error.localizedMessage ?: UNKNOWN_ERROR
                    }
                    _timeReminderState.value = TimeReminderState.Failure(errorMessage)
                }
            )
        }
    }

    fun resetTimeReminderState() {
        _timeReminderState.value = TimeReminderState.Idle
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
