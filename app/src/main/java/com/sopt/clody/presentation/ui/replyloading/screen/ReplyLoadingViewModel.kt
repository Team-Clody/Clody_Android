package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.DiaryTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReplyLoadingViewModel @Inject constructor(
    private val diaryTimeRepository: DiaryTimeRepository
) : ViewModel() {

    private val _replyLoadingState = MutableStateFlow<ReplyLoadingState>(ReplyLoadingState.Idle)
    val replyLoadingState: StateFlow<ReplyLoadingState> = _replyLoadingState

    fun getDiaryTime(year: Int, month: Int, date: Int) {
        _replyLoadingState.value = ReplyLoadingState.Loading
        viewModelScope.launch {
            val result = diaryTimeRepository.getDiaryTime(year, month, date)
            _replyLoadingState.value = result.fold(
                onSuccess = { data ->
                    val targetDateTime = LocalDateTime.of(
                        year, month, date,
                        data.HH, data.MM, data.SS
                    ).plusMinutes(1)

                    ReplyLoadingState.Success(targetDateTime)
                },
                onFailure = { ReplyLoadingState.Failure(it.message ?: "Unknown error") }
            )
        }
    }
}
