package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.request.RequestSendNotificationDto
import com.sopt.clody.data.remote.dto.response.ResponseNotificationInfoDto
import com.sopt.clody.data.repository.NotificationRepository
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
class NotificationSettingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _notificationInfoState = MutableStateFlow<NotificationInfoState>(NotificationInfoState.Idle)
    val notificationInfoState: StateFlow<NotificationInfoState> = _notificationInfoState

    private val _diaryAlarmChangeState = MutableStateFlow<DiaryAlarmChangeState>(DiaryAlarmChangeState.Idle)
    val diaryAlarmChangeState: StateFlow<DiaryAlarmChangeState> = _diaryAlarmChangeState

    private val _notificationTimeChangeState = MutableStateFlow<NotificationTimeChangeState>(NotificationTimeChangeState.Idle)
    val notificationTimeChangeState: StateFlow<NotificationTimeChangeState> = _notificationTimeChangeState

    private val _replyAlarmChangeState = MutableStateFlow<ReplyAlarmChangeState>(ReplyAlarmChangeState.Idle)
    val replyAlarmChangeState: StateFlow<ReplyAlarmChangeState> = _replyAlarmChangeState

    private val _showFailureDialog = MutableStateFlow(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog

    private val _failureDialogMessage = MutableStateFlow("")
    val failureDialogMessage: StateFlow<String> = _failureDialogMessage

    private val maxRetryCount = 3
    private var retryCount = 0

    fun getNotificationInfo() {
        if (retryCount >= maxRetryCount) return
        _notificationInfoState.value = NotificationInfoState.Loading
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _notificationInfoState.value = NotificationInfoState.Failure(FAILURE_NETWORK_MESSAGE)
                return@launch
            }
            val result = notificationRepository.getNotificationInfo()
            _notificationInfoState.value = result.fold(
                onSuccess = {
                    retryCount = 0
                    NotificationInfoState.Success(it)
                },
                onFailure = {
                    retryCount++
                    if (retryCount >= maxRetryCount) {
                        NotificationInfoState.Failure(FAILURE_TEMPORARY_MESSAGE)
                    } else {
                        if (it.message?.contains("200") == false) {
                            NotificationInfoState.Failure(FAILURE_TEMPORARY_MESSAGE)
                        } else {
                            NotificationInfoState.Failure(UNKNOWN_ERROR)
                        }
                    }
                }
            )
        }
    }

    fun changeDiaryAlarm(context: Context, notificationInfo: ResponseNotificationInfoDto, diaryAlarm: Boolean) {
        _diaryAlarmChangeState.value = DiaryAlarmChangeState.Loading
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _failureDialogMessage.value = FAILURE_NETWORK_MESSAGE
                _showFailureDialog.value = true
                return@launch
            }
            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _diaryAlarmChangeState.value = DiaryAlarmChangeState.Failure("FCM Token을 가져오는데 실패했습니다.")
                return@launch
            }
            val requestDto = RequestSendNotificationDto(
                isDiaryAlarm = diaryAlarm,
                isReplyAlarm = notificationInfo.isReplyAlarm,
                time = notificationInfo.time,
                fcmToken = fcmToken
            )
            notificationRepository.sendNotification(requestDto).fold(
                onSuccess = { _diaryAlarmChangeState.value = DiaryAlarmChangeState.Success(it) },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        UNKNOWN_ERROR
                    }
                    _showFailureDialog.value = true
                    DiaryAlarmChangeState.Failure(_failureDialogMessage.value)
                }
            )
        }
    }

    fun changeNotificationTime(context: Context, notificationInfo: ResponseNotificationInfoDto, time: String) {
        _notificationTimeChangeState.value = NotificationTimeChangeState.Loading
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _failureDialogMessage.value = FAILURE_NETWORK_MESSAGE
                _showFailureDialog.value = true
                return@launch
            }
            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _notificationTimeChangeState.value = NotificationTimeChangeState.Failure("FCM Token을 가져오는데 실패했습니다.")
                return@launch
            }
            val requestDto = RequestSendNotificationDto(
                isDiaryAlarm = notificationInfo.isDiaryAlarm,
                isReplyAlarm = notificationInfo.isReplyAlarm,
                time = time,
                fcmToken = fcmToken
            )
            notificationRepository.sendNotification(requestDto).fold(
                onSuccess = { _notificationTimeChangeState.value = NotificationTimeChangeState.Success(it) },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        UNKNOWN_ERROR
                    }
                    _showFailureDialog.value = true
                    NotificationInfoState.Failure(_failureDialogMessage.value)
                }
            )
        }
    }

    fun changeReplyAlarm(context: Context, notificationInfo: ResponseNotificationInfoDto, replyAlarm: Boolean) {
        _replyAlarmChangeState.value = ReplyAlarmChangeState.Loading
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _failureDialogMessage.value = FAILURE_NETWORK_MESSAGE
                _showFailureDialog.value = true
                return@launch
            }
            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _replyAlarmChangeState.value = ReplyAlarmChangeState.Failure("FCM Token을 가져오는데 실패했습니다.")
                return@launch
            }
            val requestDto = RequestSendNotificationDto(
                isDiaryAlarm = notificationInfo.isDiaryAlarm,
                isReplyAlarm = replyAlarm,
                time = notificationInfo.time,
                fcmToken = fcmToken
            )
            notificationRepository.sendNotification(requestDto).fold(
                onSuccess = { _replyAlarmChangeState.value = ReplyAlarmChangeState.Success(it) },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        UNKNOWN_ERROR
                    }
                    _showFailureDialog.value = true
                    ReplyAlarmChangeState.Failure(_failureDialogMessage.value)
                }
            )
        }
    }

    fun resetNotificationChangeState() {
        _notificationTimeChangeState.value = NotificationTimeChangeState.Idle
    }

    fun dismissFailureDialog() {
        _showFailureDialog.value = false
        _failureDialogMessage.value = ""
    }

    private fun getTokenFromPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("fcm_token", null)
    }

    fun convertTo12HourFormat(time: String): String {
        val (hourBefore, minuteBefore) = time.split(":").map { it.toInt() }

        val amPm = if (hourBefore < 12) "오전" else "오후"

        val hourAfter = when {
            hourBefore == 0 -> 12
            hourBefore > 12 -> hourBefore - 12
            else -> hourBefore
        }

        val minuteAfter = if (minuteBefore == 0) "00" else minuteBefore

        return String.format("$amPm ${hourAfter}시 ${minuteAfter}분")
    }

    fun convertTo24HourFormat(amPm: String, hour: String, minute: String): String {
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
