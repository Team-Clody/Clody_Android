package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.DailyDiaryListRepository
import com.sopt.clody.data.repository.DiaryListRepository
import com.sopt.clody.presentation.ui.home.screen.DeleteDiaryState
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

    private val _deleteDiaryResult = MutableStateFlow<DeleteDiaryListState>(DeleteDiaryListState.Idle)
    val deleteDiaryResult: StateFlow<DeleteDiaryListState> get() = _deleteDiaryResult

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
            _deleteDiaryResult.value = DeleteDiaryListState.Loading
            val result = dailyDiaryListRepository.deleteDailyDiary(year, month, day)
            _deleteDiaryResult.value = result.fold(
                onSuccess = {
                    DeleteDiaryListState.Success
                },
                onFailure = {
                    DeleteDiaryListState.Failure(it.message ?: "Unknown error")
                }
            )
        }
    }
}
