package com.sopt.clody.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MonthlyCalendarRepository
) : ViewModel() {
    private val _calendarData = MutableStateFlow<ApiResponse<List<MonthlyCalendarResponseDto>>?>(null)
    val monthlyCalendarData: StateFlow<ApiResponse<List<MonthlyCalendarResponseDto>>?> get() = _calendarData

    fun loadCalendarData(year: Int, month: Int) {
        viewModelScope.launch {
            val data = repository.getMonthlyCalendarData(year, month)
            _calendarData.value = data
        }
    }

}

