package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.DiaryTimeRepository
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import com.sopt.clody.presentation.utils.network.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReplyLoadingViewModel @Inject constructor(
    private val diaryTimeRepository: DiaryTimeRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _replyLoadingState = MutableStateFlow<ReplyLoadingState>(ReplyLoadingState.Idle)
    val replyLoadingState: StateFlow<ReplyLoadingState> = _replyLoadingState

    private var retryCount = 0
    private val maxRetryCount = 3

    private var lastYear: Int = 0
    private var lastMonth: Int = 0
    private var lastDate: Int = 0

    fun getDiaryTime(year: Int, month: Int, date: Int) {
        if (retryCount >= maxRetryCount) {
            return
        }

        lastYear = year
        lastMonth = month
        lastDate = date

        _replyLoadingState.value = ReplyLoadingState.Loading

        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _replyLoadingState.value = ReplyLoadingState.Failure(FAILURE_NETWORK_MESSAGE)
                return@launch
            }

            val result = diaryTimeRepository.getDiaryTime(year, month, date)
            _replyLoadingState.value = result.fold(
                onSuccess = { data ->
                    retryCount = 0
                    val targetDateTime = LocalDateTime.of(
                        year, month, date,
                        data.HH, data.MM, data.SS
                    ).plusMinutes(1)

                    ReplyLoadingState.Success(targetDateTime)
                },
                onFailure = {
                    retryCount++
                    if (retryCount >= maxRetryCount) {
                        ReplyLoadingState.Failure(FAILURE_TEMPORARY_MESSAGE)
                    } else {
                        val message = if (it.message?.contains("200") == false) {
                            FAILURE_TEMPORARY_MESSAGE
                        } else {
                            it.localizedMessage ?: UNKNOWN_ERROR
                        }
                        ReplyLoadingState.Failure(message)
                    }
                }
            )
        }
    }

    fun retryLastRequest() {
        if (retryCount < maxRetryCount) {
            _replyLoadingState.value = ReplyLoadingState.Loading
            getDiaryTime(lastYear, lastMonth, lastDate)
        }
    }
}
