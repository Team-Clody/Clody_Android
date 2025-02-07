package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.presentation.utils.extension.throttleFirst
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import com.sopt.clody.data.remote.util.NetworkUtil
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
    private val diaryTimeRepository: DiaryTimeRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _replyLoadingState = MutableStateFlow<ReplyLoadingState>(ReplyLoadingState.Idle)
    val replyLoadingState: StateFlow<ReplyLoadingState> = _replyLoadingState

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

            val result = diaryTimeRepository.getDiaryTime(year, month, date)
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<DiaryTimeResponseDto>) {
        result.fold(
            onSuccess = { data ->
                val targetDateTime = LocalDateTime.of(
                    lastYear, lastMonth, lastDate,
                    data.HH, data.mm, data.ss
                ).plusMinutes(if (data.isFirst) INITIAL_REMINDER_MINUTES else REGULAR_REMINDER_HOURS * 60)

                _replyLoadingState.value = ReplyLoadingState.Success(targetDateTime)
            },
            onFailure = { throwable ->
                _replyLoadingState.value = ReplyLoadingState.Failure(FAILURE_TEMPORARY_MESSAGE)
                val errorMessage = throwable.localizedMessage ?: UNKNOWN_ERROR
                Timber.tag("ReplyLoadingViewModel").e("API 요청 실패: %s", errorMessage)
            }
        )
    }

    fun retryLastRequest() {
        viewModelScope.launch {
            _retryFlow.emit(Unit)
        }
    }

    companion object {
        private const val INITIAL_REMINDER_MINUTES = 1L
        private const val REGULAR_REMINDER_HOURS = 12L
    }
}
