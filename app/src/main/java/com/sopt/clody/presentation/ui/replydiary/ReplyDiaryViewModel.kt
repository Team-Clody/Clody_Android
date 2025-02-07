package com.sopt.clody.presentation.ui.replydiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.ResponseReplyDiaryDto
import com.sopt.clody.data.repository.ReplyDiaryRepository
import com.sopt.clody.presentation.utils.extension.throttleFirst
import com.sopt.clody.presentation.utils.network.ErrorMessages
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
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

    private val _retryFlow = MutableSharedFlow<Unit>() //연속 클릭을 제어하기 위해 선언.

    init {
        setupRetryFlow()
    }

    private fun setupRetryFlow() {
        _retryFlow
            .throttleFirst(2000L) // 2초 동안 첫 번째 이벤트만 발행.
            .onEach { // Flow에서 발생한 이벤트를 받아서 getReplyDiaryInternal 호출.
                getReplyDiaryInternal(lastYear, lastMonth, lastDate)
            }
            .launchIn(viewModelScope) //Flow를 viewModelScope에서 실행하고 구독을 유지, 즉 viewmodel이 살아있는 동안 flow가 실행됨
    }


    fun getReplyDiary(year: Int, month: Int, date: Int) {
        lastYear = year
        lastMonth = month
        lastDate = date
        getReplyDiaryInternal(year, month, date)
    }

    private fun getReplyDiaryInternal(year: Int, month: Int, date: Int) {
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                updateState(ReplyDiaryState.Failure(FAILURE_NETWORK_MESSAGE))
                return@launch
            }

            updateState(ReplyDiaryState.Loading)

            val result = replyDiaryRepository.getReplyDiary(year, month, date)
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<ResponseReplyDiaryDto>) {
        result.fold(
            onSuccess = { data ->
                updateState(
                    ReplyDiaryState.Success(
                        content = data.content ?: "",
                        nickname = data.nickname,
                        month = data.month,
                        date = data.date
                    )
                )
            },
            onFailure = { throwable ->
                updateState(ReplyDiaryState.Failure(ErrorMessages.FAILURE_TEMPORARY_MESSAGE))
                val errorMessage = throwable.localizedMessage ?: UNKNOWN_ERROR
                Timber.tag("ReplyDiaryViewModel").e("API 요청 실패: %s", errorMessage)
            }
        )
    }

    private fun updateState(newState: ReplyDiaryState) {
        _replyDiaryState.value = newState
    }

    fun retryLastRequest() {
        viewModelScope.launch {
            _retryFlow.emit(Unit)
        }
    }
}
