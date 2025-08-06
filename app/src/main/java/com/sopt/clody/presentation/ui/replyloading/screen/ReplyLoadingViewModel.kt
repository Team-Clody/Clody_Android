package com.sopt.clody.presentation.ui.replyloading.screen

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.ad.RewardAdShower
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.util.ApiError
import com.sopt.clody.domain.repository.AdRepository
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.presentation.utils.extension.throttleFirst
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReplyLoadingViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val adRepository: AdRepository,
    private val rewardAdShower: RewardAdShower,
    private val errorMessageProvider: ErrorMessageProvider,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {

    private val _replyLoadingState = MutableStateFlow<ReplyLoadingState>(ReplyLoadingState.Idle)
    val replyLoadingState: StateFlow<ReplyLoadingState> = _replyLoadingState

    private val _isAdLoading = MutableStateFlow(false)
    val isAdLoading: StateFlow<Boolean> = _isAdLoading

    private var _isAdPreloaded = false

    private val _adErrorMessage = MutableStateFlow<String?>(null)
    val adErrorMessage: StateFlow<String?> = _adErrorMessage

    private val _isWaitingForPatchResponse = MutableStateFlow(false)
    val isWaitingForPatchResponse: StateFlow<Boolean> = _isWaitingForPatchResponse

    private val _isAdCompleted = MutableStateFlow(false)
    val isAdCompleted: StateFlow<Boolean> = _isAdCompleted

    private val _isFirstDiary = MutableStateFlow(false)
    val isFirstDiary: StateFlow<Boolean> = _isFirstDiary

    private var lastYear: Int = 0
    private var lastMonth: Int = 0
    private var lastDate: Int = 0

    private val _retryFlow = MutableSharedFlow<Unit>()

    init {
        setupRetryFlow()
        preloadAd()
    }

    private fun setupRetryFlow() {
        _retryFlow
            .throttleFirst(2000L)
            .onEach { getDiaryTimeInternal(lastYear, lastMonth, lastDate) }
            .launchIn(viewModelScope)
    }

    fun getDiaryTime(year: Int, month: Int, date: Int) {
        lastYear = year
        lastMonth = month
        lastDate = date
        getDiaryTimeInternal(year, month, date)
    }

    private fun getDiaryTimeInternal(year: Int, month: Int, date: Int) {
        _replyLoadingState.value = ReplyLoadingState.Loading

        viewModelScope.launch {
            val isConnected = networkConnectivityObserver.networkStatus.first() == NetworkStatus.Available
            if (!isConnected) {
                _replyLoadingState.value = ReplyLoadingState.Failure(errorMessageProvider.getNetworkError())
                return@launch
            }

            val result = diaryRepository.getDiaryTime(year, month, date)
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<DiaryTimeResponseDto>) {
        result.fold(
            onSuccess = { data ->
                val (y, m, d) = data.date.split("-").map { it.toInt() }
                var targetDateTime = LocalDateTime.of(y, m, d, data.HH, data.mm, data.ss)
                    .plusMinutes(if (data.isFirst) INITIAL_REMINDER_MINUTES else REGULAR_REMINDER_HOURS * 60)

                if (_isAdCompleted.value || data.isFromAd) {
                    targetDateTime = LocalDateTime.now()
                }

                _isFirstDiary.value = data.isFirst
                _replyLoadingState.value = ReplyLoadingState.Success(targetDateTime)
                _isWaitingForPatchResponse.value = false
            },
            onFailure = { throwable ->
                val message = if (throwable is ApiError) {
                    errorMessageProvider.getApiError(throwable)
                } else {
                    errorMessageProvider.getTemporaryError()
                }
                _replyLoadingState.value = ReplyLoadingState.Failure(message)
                Timber.tag("ReplyLoadingViewModel").e(throwable, "DiaryTime API 요청 실패")
            },
        )
    }

    private fun preloadAd() {
        if (_isAdPreloaded) return

        viewModelScope.launch {
            val result = adRepository.loadRewardedAd()
            _isAdPreloaded = result.isSuccess
        }
    }

    fun loadAndShowRewardedAd(activity: Activity) {
        if (_isAdLoading.value) return

        _isAdLoading.value = true
        viewModelScope.launch {
            val startAdResult = adRepository.startAd(lastYear, lastMonth, lastDate)
            if (startAdResult.isFailure) {
                _isAdLoading.value = false
                _adErrorMessage.value = errorMessageProvider.getTemporaryError()
                return@launch
            }

            if (_isAdPreloaded) {
                _isAdLoading.value = false
                showRewardedAd(activity)
            } else {
                val loadAdResult = adRepository.loadRewardedAd()
                _isAdLoading.value = false
                if (loadAdResult.isSuccess) {
                    _isAdPreloaded = true
                    showRewardedAd(activity)
                } else {
                    _adErrorMessage.value = errorMessageProvider.getTemporaryError()
                }
            }
        }
    }

    private fun showRewardedAd(activity: Activity) {
        rewardAdShower.showAd(
            activity,
            onAdRewarded = {
                viewModelScope.launch {
                    _isWaitingForPatchResponse.value = true
                    adRepository.endAd(lastYear, lastMonth, lastDate).onSuccess {
                        _replyLoadingState.value = ReplyLoadingState.Success(LocalDateTime.now())
                        _isWaitingForPatchResponse.value = false
                    }.onFailure {
                        _isWaitingForPatchResponse.value = false
                    }
                }
            },
            onAdDismissed = {
                _isAdPreloaded = false
                preloadAd()
            },
        )
    }

    fun retryLastRequest() {
        viewModelScope.launch {
            _retryFlow.emit(Unit)
        }
    }

    fun clearAdErrorMessage() {
        _adErrorMessage.value = null
    }

    companion object {
        private const val INITIAL_REMINDER_MINUTES = 1L
        private const val REGULAR_REMINDER_HOURS = 12L
    }
}
