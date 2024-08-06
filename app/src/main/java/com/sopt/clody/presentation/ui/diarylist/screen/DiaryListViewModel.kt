package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.DailyDiaryListRepository
import com.sopt.clody.data.repository.DiaryListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val diaryListRepository: DiaryListRepository,
    private val dailyDiaryListRepository: DailyDiaryListRepository
) : ViewModel() {

    private val _diaryListState = MutableStateFlow<DiaryListState>(DiaryListState.Idle)
    val diaryListState: StateFlow<DiaryListState> = _diaryListState

    private val _deleteDiaryState = MutableStateFlow<DeleteDiaryState>(DeleteDiaryState.Idle)
    val deleteDiaryState: StateFlow<DeleteDiaryState> get() = _deleteDiaryState

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

    fun deleteDailyDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _deleteDiaryState.value = DeleteDiaryState.Loading
            val result = dailyDiaryListRepository.deleteDailyDiary(year, month, day)
            _deleteDiaryState.value = result.fold(
                onSuccess = {
                    DeleteDiaryState.Success
                },
                onFailure = {
                    DeleteDiaryState.Failure(it.message ?: "Unknown error")
                }
            )
        }
    }
}
