package com.sopt.clody.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.DailyDiariesRepository
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val calendarRepository: MonthlyCalendarRepository,
    private val diariesRepository: DailyDiariesRepository
) : ViewModel() {
    private val _calendarData = MutableStateFlow<Result<MonthlyCalendarResponseDto>?>(null)
    val monthlyCalendarData: StateFlow<Result<MonthlyCalendarResponseDto>?> get() = _calendarData
    private val _dailyDiariesData = MutableStateFlow<Result<DailyDiariesResponseDto>?>(null)
    val dailyDiariesData: StateFlow<Result<DailyDiariesResponseDto>?> get() = _dailyDiariesData

    fun loadCalendarData(year: Int, month: Int) {
        viewModelScope.launch {
            val result = calendarRepository.getMonthlyCalendarData(year, month)
            _calendarData.value = result
        }
    }

    fun loadDailyDiariesData(year: Int, month: Int, date: Int) {
        viewModelScope.launch {
            val result = diariesRepository.getDailyDiariesData(year, month, date)
            _dailyDiariesData.value = result
        }
    }
}
