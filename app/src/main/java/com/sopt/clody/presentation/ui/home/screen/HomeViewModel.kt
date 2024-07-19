package com.sopt.clody.presentation.ui.home.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.DailyDiariesRepository
import com.sopt.clody.data.repository.DailyDiaryListRepository
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val calendarRepository: MonthlyCalendarRepository,
    private val diariesRepository: DailyDiariesRepository,
    private val dailyDiaryListRepository: DailyDiaryListRepository
) : ViewModel() {

    private val _calendarData = MutableStateFlow<Result<MonthlyCalendarResponseDto>>(Result.failure(Exception("Initial state")))
    val monthlyCalendarData: StateFlow<Result<MonthlyCalendarResponseDto>> get() = _calendarData

    private val _dailyDiariesData = MutableStateFlow<Result<DailyDiariesResponseDto>>(Result.failure(Exception("Initial state")))
    val dailyDiariesData: StateFlow<Result<DailyDiariesResponseDto>> get() = _dailyDiariesData

    private val _deleteDiaryResult = MutableStateFlow<DeleteDiaryState>(DeleteDiaryState.Idle)
    val deleteDiaryResult: StateFlow<DeleteDiaryState> get() = _deleteDiaryResult

    private val _diaryCount = MutableStateFlow(0)
    val diaryCount: StateFlow<Int> get() = _diaryCount

    private val _replyStatus = MutableStateFlow("UNREADY")
    val replyStatus: StateFlow<String> get() = _replyStatus

    private val _isToday = MutableStateFlow(false)
    val isToday: StateFlow<Boolean> get() = _isToday

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> get() = _selectedDate

    init {
        updateIsToday(LocalDate.now().year, LocalDate.now().monthValue)
        loadCalendarData(LocalDate.now().year, LocalDate.now().monthValue)
        updateSelectedDate(LocalDate.now())
    }

    fun loadCalendarData(year: Int, month: Int) {
        viewModelScope.launch {
            val result = calendarRepository.getMonthlyCalendarData(year, month)
            _calendarData.value = result
            result.onSuccess { data ->
                updateDiaryState(data.diaries)
                Log.d("HomeViewModel", "Loaded calendar data: ${data.diaries.size} diaries, replyStatus: ${_replyStatus.value}")
            }.onFailure {
                Log.e("HomeViewModel", "Failed to load calendar data: ${it.message}")
            }
        }
        updateIsToday(year, month)
    }

    fun updateIsToday(year: Int, month: Int) {
        val currentDate = LocalDate.now()
        _isToday.value = currentDate.year == year && currentDate.monthValue == month
    }

    fun loadDailyDiariesData(year: Int, month: Int, date: Int) {
        viewModelScope.launch {
            val result = diariesRepository.getDailyDiariesData(year, month, date)
            _dailyDiariesData.value = result
            result.onSuccess { data ->
                Log.d("HomeViewModel", "Loaded daily diaries data for $year-$month-$date: ${data.diaries.size} diaries")
            }.onFailure {
                Log.e("HomeViewModel", "Failed to load daily diaries data for $year-$month-$date: ${it.message}")
            }
        }
    }

    fun deleteDailyDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _deleteDiaryResult.value = DeleteDiaryState.Loading
            val result = dailyDiaryListRepository.deleteDailyDiary(year, month, day)
            _deleteDiaryResult.value = result.fold(
                onSuccess = {
                    loadCalendarData(year, month)
                    loadDailyDiariesData(year, month, day)
                    DeleteDiaryState.Success
                },
                onFailure = {
                    DeleteDiaryState.Failure(it.message ?: "Unknown error")
                }
            )
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        loadDailyDiariesData(date.year, date.monthValue, date.dayOfMonth)
    }

    fun updateDiaryState(diaries: List<MonthlyCalendarResponseDto.Diary>) {
        val selectedDiary = diaries.getOrNull(_selectedDate.value.dayOfMonth - 1)
        _diaryCount.value = selectedDiary?.diaryCount ?: 0
        _replyStatus.value = selectedDiary?.replyStatus ?: "UNREADY"
    }
}
