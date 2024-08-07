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

    private val _selectedDiaryYear = MutableStateFlow(0)
    val selectedDiaryYear: StateFlow<Int> = _selectedDiaryYear

    private val _selectedDiaryMonth = MutableStateFlow(0)
    val selectedDiaryMonth: StateFlow<Int> = _selectedDiaryMonth

    private val _selectedDiaryDay = MutableStateFlow(0)
    val selectedDiaryDay: StateFlow<Int> = _selectedDiaryDay

    private val _selectedDiaryDayOfWeek = MutableStateFlow("")
    val selectedDiaryDayOfWeek: StateFlow<String> = _selectedDiaryDayOfWeek

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
        _selectedDiaryYear.value = diaryDate[0].toInt()
        _selectedDiaryMonth.value = diaryDate[1].toInt()
        _selectedDiaryDay.value = diaryDate[2].toInt()
        _selectedDiaryDayOfWeek.value = getDayOfWeek(_selectedDiaryYear.value, _selectedDiaryMonth.value, _selectedDiaryDay.value)
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
}
