package com.sopt.clody.presentation.ui.replydiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.util.ApiError
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
import javax.inject.Inject

@HiltViewModel
class ReplyDiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val errorMessageProvider: ErrorMessageProvider,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {

    private val _replyDiaryState = MutableStateFlow<ReplyDiaryState>(ReplyDiaryState.Idle)
    val replyDiaryState: StateFlow<ReplyDiaryState> = _replyDiaryState

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
                getReplyDiaryInternal(lastYear, lastMonth, lastDate)
            }
            .launchIn(viewModelScope)
    }

    fun getReplyDiary(year: Int, month: Int, date: Int) {
        lastYear = year
        lastMonth = month
        lastDate = date
        getReplyDiaryInternal(year, month, date)
    }

    private fun getReplyDiaryInternal(year: Int, month: Int, date: Int) {
        viewModelScope.launch {
            val isConnected = networkConnectivityObserver.networkStatus.first() == NetworkStatus.Available
            if (!isConnected) {
                updateState(ReplyDiaryState.Failure(errorMessageProvider.getNetworkError()))
                return@launch
            }

            updateState(ReplyDiaryState.Loading)

            val result = diaryRepository.getReplyDiary(year, month, date)
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<ReplyDiaryResponseDto>) {
        result.fold(
            onSuccess = { data ->
                updateState(
                    ReplyDiaryState.Success(
                        content = data.content.orEmpty(),
                        nickname = data.nickname,
                        month = data.month,
                        date = data.date,
                    ),
                )
            },
            onFailure = { throwable ->
                val message = when (throwable) {
                    is ApiError -> errorMessageProvider.getApiError(throwable)
                    else -> errorMessageProvider.getTemporaryError()
                }
                updateState(ReplyDiaryState.Failure(message))
                Timber.tag("ReplyDiaryViewModel").e(throwable, "API 요청 실패")
            },
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
