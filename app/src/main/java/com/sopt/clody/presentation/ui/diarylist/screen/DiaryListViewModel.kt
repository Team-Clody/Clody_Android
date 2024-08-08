package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.repository.DailyDiaryListRepository
import com.sopt.clody.data.repository.DiaryListRepository
import com.sopt.clody.presentation.utils.extension.getDayOfWeek
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

    private val _selectedDiaryDate = MutableStateFlow(DiaryDate())
    val selectedDiaryDate: StateFlow<DiaryDate> = _selectedDiaryDate

    private val _diaryDeleteState = MutableStateFlow<DiaryDeleteState>(DiaryDeleteState.Idle)
    val diaryDeleteState: StateFlow<DiaryDeleteState> get() = _diaryDeleteState

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

    fun setSelectedDiaryDate(selectedDiaryDate: String) {
        val diaryDate = selectedDiaryDate.split("-")
        val year = diaryDate[0].toInt()
        val month = diaryDate[1].toInt()
        val day = diaryDate[2].toInt()
        val dayOfWeek = getDayOfWeek(year, month, day)
        _selectedDiaryDate.value = DiaryDate(year, month, day, dayOfWeek)
    }

    fun deleteDailyDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _diaryDeleteState.value = DiaryDeleteState.Loading
            val result = dailyDiaryListRepository.deleteDailyDiary(year, month, day)
            _diaryDeleteState.value = result.fold(
                onSuccess = {
                    DiaryDeleteState.Success
                },
                onFailure = {
                    DiaryDeleteState.Failure(it.message ?: "Unknown error")
                }
            )
        }
    }

    data class DiaryDate(
        val year: Int = 0,
        val month: Int = 0,
        val day: Int = 0,
        val dayOfWeek: String = ""
    )
}
