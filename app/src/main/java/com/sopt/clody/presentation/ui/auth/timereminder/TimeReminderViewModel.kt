package com.sopt.clody.presentation.ui.auth.timereminder

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.domain.repository.NotificationRepository
import com.sopt.clody.presentation.utils.extension.TimePeriod
import com.sopt.clody.presentation.utils.extension.convertUTZtoKST
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeReminderViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val errorMessageProvider: ErrorMessageProvider,
) : ViewModel() {

    private val _timeReminderState = MutableStateFlow<TimeReminderState>(TimeReminderState.Idle)
    val timeReminderState: StateFlow<TimeReminderState> = _timeReminderState

    var selectedTime by mutableStateOf("21:30")
        private set

    fun sendNotification(context: Context, isPermissionGranted: Boolean) {
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _timeReminderState.value = TimeReminderState.Failure(errorMessageProvider.getNetworkError())
                return@launch
            }

            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _timeReminderState.value = TimeReminderState.Failure("FCM 못가져옴")
                return@launch
            }

            val requestDto = SendNotificationRequestDto(
                isDiaryAlarm = isPermissionGranted,
                isDraftAlarm = false,
                isReplyAlarm = isPermissionGranted,
                time = selectedTime,
                fcmToken = fcmToken,
            )

            _timeReminderState.value = TimeReminderState.Loading
            notificationRepository.sendNotification(requestDto).fold(
                onSuccess = {
                    _timeReminderState.value = TimeReminderState.Success("알림 전송 성공")
                },
                onFailure = { error ->
                    val errorMessage = if (error.message?.contains("200") == false) {
                        errorMessageProvider.getTemporaryError()
                    } else {
                        error.localizedMessage ?: errorMessageProvider.getUnknownError()
                    }
                    _timeReminderState.value = TimeReminderState.Failure(errorMessage)
                },
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

    fun setSelectedTime(period: TimePeriod, hour: String, minute: String) {
        selectedTime = convertUTZtoKST(timePeriod = period, hour = hour, minute = minute)
    }
}
