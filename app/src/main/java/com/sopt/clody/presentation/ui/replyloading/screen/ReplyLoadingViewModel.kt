package com.sopt.clody.presentation.ui.replyloading.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.DiaryTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
                    val newHH = data.HH
                    val newMM = data.MM + 1
                    val finalMM = newMM % 60
                    val finalHH = (newHH + newMM / 60) % 24
                    ReplyLoadingState.Success(
                        HH = finalHH,
                        MM = finalMM,
                        SS = data.SS
                    )
                },
                onFailure = { ReplyLoadingState.Failure(it.message ?: "Unknown error") }
            )
        }
    }
}
