package com.sopt.clody.presentation.ui.replydiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.ReplyDiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyDiaryViewModel @Inject constructor(
    private val replyDiaryRepository: ReplyDiaryRepository
) : ViewModel() {

    private val _replyDiaryState = MutableStateFlow<ReplyDiaryState>(ReplyDiaryState.Idle)
    val replyDiaryState: StateFlow<ReplyDiaryState> = _replyDiaryState

    fun getReplyDiary(year: Int, month: Int, date: Int) {
        _replyDiaryState.value = ReplyDiaryState.Loading
        viewModelScope.launch {
            val result = replyDiaryRepository.getReplyDiary(year, month, date)
            _replyDiaryState.value = result.fold(
                onSuccess = { data ->
                    ReplyDiaryState.Success(
                        content = data.content,
                        nickname = data.nickname,
                        month = data.month,
                        date = data.date
                    )
                },
                onFailure = { ReplyDiaryState.Failure(it.message ?: "Unknown error") }
            )
        }
    }
}
