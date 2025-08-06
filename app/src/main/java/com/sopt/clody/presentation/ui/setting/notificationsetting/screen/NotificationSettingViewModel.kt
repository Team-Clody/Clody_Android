package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.domain.Notification
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
class NotificationSettingViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val errorMessageProvider: ErrorMessageProvider,
) : ViewModel() {

    private val _diaryAlarm = MutableStateFlow(false)
    val diaryAlarm: StateFlow<Boolean> = _diaryAlarm

    private val _draftAlarm = MutableStateFlow(false)
    val draftAlarm: StateFlow<Boolean> = _draftAlarm

    private val _replyAlarm = MutableStateFlow(false)
    val replyAlarm: StateFlow<Boolean> = _replyAlarm

    private val _notificationTime = MutableStateFlow("")
    val notificationTime: StateFlow<String> = _notificationTime

    private val _notificationInfoState = MutableStateFlow<NotificationInfoState>(NotificationInfoState.Idle)
    val notificationInfoState: StateFlow<NotificationInfoState> = _notificationInfoState

    private val _notificationChangeState = MutableStateFlow<NotificationChangeState>(NotificationChangeState.Idle)
    val notificationChangeState: StateFlow<NotificationChangeState> = _notificationChangeState

    private val _notificationTimeChangeState = MutableStateFlow<NotificationTimeChangeState>(NotificationTimeChangeState.Idle)
    val notificationTimeChangeState: StateFlow<NotificationTimeChangeState> = _notificationTimeChangeState

    private val _showFailureDialog = MutableStateFlow(false)
    val showFailureDialog: StateFlow<Boolean> = _showFailureDialog

    private val _failureDialogMessage = MutableStateFlow("")
    val failureDialogMessage: StateFlow<String> = _failureDialogMessage

    private val maxRetryCount = 3
    private var retryCount = 0

    init {
        getNotificationInfo()
    }

    fun getNotificationInfo() {
        if (retryCount >= maxRetryCount) return
        _notificationInfoState.value = NotificationInfoState.Loading
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _notificationInfoState.value = NotificationInfoState.Failure(errorMessageProvider.getNetworkError())
                return@launch
            }
            val result = notificationRepository.getNotificationInfo()
            _notificationInfoState.value = result.fold(
                onSuccess = {
                    retryCount = 0
                    _diaryAlarm.value = it.isDiaryAlarm
                    _draftAlarm.value = it.isDraftAlarm
                    _replyAlarm.value = it.isReplyAlarm
                    _notificationTime.value = it.time
                    NotificationInfoState.Success(it)
                },
                onFailure = {
                    retryCount++
                    if (retryCount >= maxRetryCount) {
                        NotificationInfoState.Failure(errorMessageProvider.getTemporaryError())
                    } else {
                        if (it.message?.contains("200") == false) {
                            NotificationInfoState.Failure(errorMessageProvider.getTemporaryError())
                        } else {
                            NotificationInfoState.Failure(errorMessageProvider.getUnknownError())
                        }
                    }
                },
            )
        }
    }

    fun changeAlarm(context: Context, notificationType: Notification) {
        _notificationChangeState.value = NotificationChangeState.Loading
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _failureDialogMessage.value = errorMessageProvider.getNetworkError()
                _showFailureDialog.value = true
                return@launch
            }

            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _notificationChangeState.value = NotificationChangeState.Failure("FCM Token을 가져오는데 실패했습니다.")
                return@launch
            }

            val requestDto = SendNotificationRequestDto(
                isDiaryAlarm = when (notificationType) {
                    Notification.DIARY -> !_diaryAlarm.value
                    else -> _diaryAlarm.value
                },
                isDraftAlarm = when (notificationType) {
                    Notification.DRAFT -> !_draftAlarm.value
                    else -> _draftAlarm.value
                },
                isReplyAlarm = when (notificationType) {
                    Notification.REPLY -> !_replyAlarm.value
                    else -> _replyAlarm.value
                },
                time = _notificationTime.value,
                fcmToken = fcmToken,
            )

            notificationRepository.sendNotification(requestDto).fold(
                onSuccess = {
                    _notificationChangeState.value = NotificationChangeState.Success(it)
                    getNotificationInfo()
                },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        errorMessageProvider.getTemporaryError()
                    } else {
                        errorMessageProvider.getUnknownError()
                    }
                    _showFailureDialog.value = true
                    _notificationChangeState.value = NotificationChangeState.Failure(_failureDialogMessage.value)
                },
            )
        }
    }

    fun changeNotificationTime(context: Context, timePeriod: TimePeriod, hour: String, minute: String) {
        _notificationTimeChangeState.value = NotificationTimeChangeState.Loading
        viewModelScope.launch {
            if (networkConnectivityObserver.networkStatus.first() == NetworkStatus.Unavailable) {
                _failureDialogMessage.value = errorMessageProvider.getNetworkError()
                _showFailureDialog.value = true
                return@launch
            }
            val fcmToken = getTokenFromPreferences(context)
            if (fcmToken.isNullOrBlank()) {
                _notificationTimeChangeState.value = NotificationTimeChangeState.Failure("FCM Token을 가져오는데 실패했습니다.")
                return@launch
            }
            val requestDto = SendNotificationRequestDto(
                isDiaryAlarm = _diaryAlarm.value,
                isDraftAlarm = _draftAlarm.value,
                isReplyAlarm = _replyAlarm.value,
                time = convertUTZtoKST(timePeriod, hour, minute),
                fcmToken = fcmToken,
            )
            notificationRepository.sendNotification(requestDto).fold(
                onSuccess = {
                    _notificationTimeChangeState.value = NotificationTimeChangeState.Success(it)
                    getNotificationInfo()
                },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        errorMessageProvider.getTemporaryError()
                    } else {
                        errorMessageProvider.getUnknownError()
                    }
                    _showFailureDialog.value = true
                    _notificationTimeChangeState.value = NotificationTimeChangeState.Failure(_failureDialogMessage.value)
                },
            )
        }
    }

    fun resetNotificationTimeChangeState() {
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
}
