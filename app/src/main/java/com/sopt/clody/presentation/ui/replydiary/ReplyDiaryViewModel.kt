package com.sopt.clody.presentation.ui.replydiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.ReplyDiaryRepository
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import com.sopt.clody.presentation.utils.network.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyDiaryViewModel @Inject constructor(
    private val replyDiaryRepository: ReplyDiaryRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _replyDiaryState = MutableStateFlow<ReplyDiaryState>(ReplyDiaryState.Idle)
    val replyDiaryState: StateFlow<ReplyDiaryState> = _replyDiaryState

    private var lastYear: Int = 0
    private var lastMonth: Int = 0
    private var lastDate: Int = 0

    private var retryCount = 0
    private val maxRetryCount = 3

    fun getReplyDiary(year: Int, month: Int, date: Int) {
        if (retryCount >= maxRetryCount) {
            return
        }

        lastYear = year
        lastMonth = month
        lastDate = date

        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _replyDiaryState.value = ReplyDiaryState.Failure(FAILURE_NETWORK_MESSAGE)
                return@launch
            }

            _replyDiaryState.value = ReplyDiaryState.Loading
            val result = replyDiaryRepository.getReplyDiary(year, month, date)
            _replyDiaryState.value = result.fold(
                onSuccess = { data ->
                    retryCount = 0
                    ReplyDiaryState.Success(
                        content = data.content ?: "", //content가 널이 아님을 보장함, 널이면 onFailure로 감
                        nickname = data.nickname,
                        month = data.month,
                        date = data.date
                    )
                },
                onFailure = {
                    retryCount++
                    if (retryCount >= maxRetryCount) {
                        ReplyDiaryState.Failure(FAILURE_TEMPORARY_MESSAGE)
                    } else {
                        val message = it.localizedMessage ?: UNKNOWN_ERROR
                        ReplyDiaryState.Failure(message)
                    }
                }
            )
        }
    }

    fun retryLastRequest() {
        if (retryCount < maxRetryCount) {
            getReplyDiary(lastYear, lastMonth, lastDate)
        }
    }
}
