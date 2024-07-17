package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.diarylist.ResponseMonthlyDiaryDto
import com.sopt.clody.data.repository.DiaryListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val diaryListRepository: DiaryListRepository
) : ViewModel() {

    private val _diaryListState = MutableStateFlow<DiaryListState>(DiaryListState.Idle)
    val diaryListState: StateFlow<DiaryListState> = _diaryListState

    fun fetchMonthlyDiary(year: Int, month: Int) {
        _diaryListState.value = DiaryListState.Loading
        viewModelScope.launch {
            val result = diaryListRepository.getMonthlyDiary(year, month)
            _diaryListState.value = result.fold(
                onSuccess = { DiaryListState.Success(it) },
                onFailure = { DiaryListState.Failure(it.message ?: "Unknown error") }
            )
        }
    }
}
