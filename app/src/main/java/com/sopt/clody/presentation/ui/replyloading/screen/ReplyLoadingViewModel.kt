package com.sopt.clody.presentation.ui.replyloading.screen

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.RewardAdShower
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.util.NetworkUtil
import com.sopt.clody.domain.repository.AdRepository
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.presentation.utils.extension.throttleFirst
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val networkUtil: NetworkUtil,
    private val rewardAdShower: RewardAdShower
) : ViewModel() {

    private val _replyLoadingState = MutableStateFlow<ReplyLoadingState>(ReplyLoadingState.Idle)
    val replyLoadingState: StateFlow<ReplyLoadingState> = _replyLoadingState

    private val _isAdLoading = MutableStateFlow(false)
    val isAdLoading: StateFlow<Boolean> = _isAdLoading

    private val _adErrorMessage = MutableStateFlow<String?>(null)
    val adErrorMessage: StateFlow<String?> = _adErrorMessage

    // 응답 대기 상태
    private val _isWaitingForPatchResponse = MutableStateFlow(false)
    val isWaitingForPatchResponse: StateFlow<Boolean> = _isWaitingForPatchResponse

    // 응답 완료
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
    }

    private fun setupRetryFlow() {
        _retryFlow
            .throttleFirst(2000L)
            .onEach {
                getDiaryTimeInternal(lastYear, lastMonth, lastDate)
            }
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
            if (!networkUtil.isNetworkAvailable()) {
                _replyLoadingState.value = ReplyLoadingState.Failure(FAILURE_NETWORK_MESSAGE)
                return@launch
            }

            val result = diaryRepository.getDiaryTime(year, month, date)
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<DiaryTimeResponseDto>) {
        result.fold(
            onSuccess = { data ->
                val diaryWrittenDay = data.date.split("-")
                var targetDateTime = LocalDateTime.of(
                    diaryWrittenDay[0].toInt(), diaryWrittenDay[1].toInt(), diaryWrittenDay[2].toInt(),
                    data.HH, data.mm, data.ss
                ).plusMinutes(if (data.isFirst) INITIAL_REMINDER_MINUTES else REGULAR_REMINDER_HOURS * 60)

                if (_isAdCompleted.value || data.isFromAd) {
                    targetDateTime = LocalDateTime.now()
                }

                _isFirstDiary.value = data.isFirst
                _replyLoadingState.value = ReplyLoadingState.Success(targetDateTime)
                _isWaitingForPatchResponse.value = false
            },
            onFailure = { throwable ->
                _replyLoadingState.value = ReplyLoadingState.Failure(FAILURE_TEMPORARY_MESSAGE)
                val errorMessage = throwable.localizedMessage ?: UNKNOWN_ERROR
                Timber.tag("ReplyLoadingViewModel").e("API 요청 실패: %s", errorMessage)
            }
        )
    }

    //  광고 시작 → 광고 시청 → 광고 종료 후 응답 업데이트
    fun loadAndShowRewardedAd(activity: Activity) {
        if (_isAdLoading.value) return

        _isAdLoading.value = true
        viewModelScope.launch {
            Timber.d("광고 시작 API 호출: year=$lastYear, month=$lastMonth, day=$lastDate")

            val startAdResult = adRepository.startAd(lastYear, lastMonth, lastDate)
            if (startAdResult.isFailure) {
                _isAdLoading.value = false
                _adErrorMessage.value = "잠시 후 다시 시도해주세요!"
                Timber.e("📌 광고 시작 실패: ${startAdResult.exceptionOrNull()?.message}")
                return@launch
            }

            Timber.d("광고 시작 성공, 광고 로드 시작")

            val loadAdResult = adRepository.loadRewardedAd()
            if (loadAdResult.isFailure) {
                _isAdLoading.value = false
                _adErrorMessage.value = "잠시 후 다시 시도해주세요!"
                return@launch
            }
            _isAdLoading.value = false
            showRewardedAdAndReloadDiaryTime(activity)
        }
    }

    private fun showRewardedAdAndReloadDiaryTime(activity: Activity) {
        rewardAdShower.showAd(
            activity,
            onAdRewarded = {
                viewModelScope.launch {
                    _isWaitingForPatchResponse.value = true

                    adRepository.endAd(lastYear, lastMonth, lastDate).onSuccess {
                        Timber.d("PATCH 응답 도착 - 답장이 준비됨")

                        _replyLoadingState.value = ReplyLoadingState.Success(LocalDateTime.now())
                        _isWaitingForPatchResponse.value = false
                    }.onFailure {
                        Timber.e("종료 실패: ${it.localizedMessage}")
                        _isWaitingForPatchResponse.value = false
                    }
                }
            },
            onAdDismissed = {}
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
