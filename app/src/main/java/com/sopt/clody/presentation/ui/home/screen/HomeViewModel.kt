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

    init {
        updateIsToday(LocalDate.now().year, LocalDate.now().monthValue)
    }

    fun loadCalendarData(year: Int, month: Int) {
        viewModelScope.launch {
            val result = calendarRepository.getMonthlyCalendarData(year, month)
            _calendarData.value = result
            result.onSuccess { data ->
                _diaryCount.value = data.diaries.size
                _replyStatus.value = data.diaries.firstOrNull()?.replyStatus ?: "UNREADY"
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
        }
    }

    fun deleteDailyDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _deleteDiaryResult.value = DeleteDiaryState.Loading
            val result = dailyDiaryListRepository.deleteDailyDiary(year, month, day)
            _deleteDiaryResult.value = result.fold(
                onSuccess = {
                    DeleteDiaryState.Success("삭제가 완료되었습니다.")
                },
                onFailure = {
                    DeleteDiaryState.Failure(it.message ?: "Unknown error")
                }
            )
            loadCalendarData(year, month)
            loadDailyDiariesData(year, month, day)
        }
    }
}
