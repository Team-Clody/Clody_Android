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

    private val _calendarUiState = MutableStateFlow<CalendarState<MonthlyCalendarResponseDto>>(CalendarState.Idle)
    val calendarUiState: StateFlow<CalendarState<MonthlyCalendarResponseDto>> get() = _calendarUiState

    private val _dailyDiariesUiState = MutableStateFlow<DailyDiariesState<DailyDiariesResponseDto>>(DailyDiariesState.Idle)
    val dailyDiariesUiState: StateFlow<DailyDiariesState<DailyDiariesResponseDto>> get() = _dailyDiariesUiState

    private val _deleteDiaryUiState = MutableStateFlow<DeleteDiaryState>(DeleteDiaryState.Idle)
    val deleteDiaryState: StateFlow<DeleteDiaryState> get() = _deleteDiaryUiState

    private val _diaryCount = MutableStateFlow(0)
    val diaryCount: StateFlow<Int> get() = _diaryCount

    private val _replyStatus = MutableStateFlow("UNREADY")
    val replyStatus: StateFlow<String> get() = _replyStatus

    private val _isToday = MutableStateFlow(false)
    val isToday: StateFlow<Boolean> get() = _isToday

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> get() = _selectedDate

    private val _showYearMonthPickerState = MutableStateFlow(false)
    val showYearMonthPickerState: StateFlow<Boolean> get() = _showYearMonthPickerState

    private val _showDiaryDeleteState = MutableStateFlow(false)
    val showDiaryDeleteState: StateFlow<Boolean> get() = _showDiaryDeleteState

    private val _showDiaryDeleteDialog = MutableStateFlow(false)
    val showDiaryDeleteDialog: StateFlow<Boolean> get() = _showDiaryDeleteDialog

    private val _selectedYearInCalendar = MutableStateFlow(LocalDate.now().year)
    val selectedYearInCalendar: StateFlow<Int> get() = _selectedYearInCalendar

    private val _selectedMonthInCalendar = MutableStateFlow(LocalDate.now().monthValue)
    val selectedMonthInCalendar: StateFlow<Int> get() = _selectedMonthInCalendar

    init {
        updateIsToday(LocalDate.now().year, LocalDate.now().monthValue)
        loadCalendarData(LocalDate.now().year, LocalDate.now().monthValue)
        updateSelectedDate(LocalDate.now())
    }

    private var isCalendarDataLoaded = false
    private var isDailyDiariesDataLoaded = false

    fun loadCalendarData(year: Int, month: Int) {
        if (isCalendarDataLoaded) return
        viewModelScope.launch {
            _calendarUiState.value = CalendarState.Loading
            val result = calendarRepository.getMonthlyCalendarData(year, month)
            _calendarUiState.value = result.fold(
                onSuccess = { data ->
                    updateDiaryState(data.diaries)
                    isCalendarDataLoaded = true
                    CalendarState.Success(data)
                },
                onFailure = { CalendarState.Error(it.message ?: "Unknown error") }
            )
        }
        updateIsToday(year, month)
    }


    fun loadDailyDiariesData(year: Int, month: Int, date: Int) {
        if (isDailyDiariesDataLoaded) return
        viewModelScope.launch {
            _dailyDiariesUiState.value = DailyDiariesState.Loading
            val result = diariesRepository.getDailyDiariesData(year, month, date)
            _dailyDiariesUiState.value = result.fold(
                onSuccess = {
                    isDailyDiariesDataLoaded = true
                    DailyDiariesState.Success(it)
                },
                onFailure = { DailyDiariesState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun deleteDailyDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _deleteDiaryUiState.value = DeleteDiaryState.Loading
            val result = dailyDiaryListRepository.deleteDailyDiary(year, month, day)
            _deleteDiaryUiState.value = result.fold(
                onSuccess = {
                    isCalendarDataLoaded = false
                    isDailyDiariesDataLoaded = false
                    loadCalendarData(year, month)
                    loadDailyDiariesData(year, month, day)
                    DeleteDiaryState.Success
                },
                onFailure = { DeleteDiaryState.Failure(it.message ?: "Unknown error") }
            )
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        isDailyDiariesDataLoaded = false
        loadDailyDiariesData(date.year, date.monthValue, date.dayOfMonth)
    }

    fun updateDiaryState(diaries: List<MonthlyCalendarResponseDto.Diary>) {
        val selectedDiary = diaries.getOrNull(_selectedDate.value.dayOfMonth - 1)
        _diaryCount.value = selectedDiary?.diaryCount ?: 0
        _replyStatus.value = selectedDiary?.replyStatus ?: "UNREADY"
    }

    fun updateIsToday(year: Int, month: Int) {
        val currentDate = LocalDate.now()
        _isToday.value = currentDate.year == year && currentDate.monthValue == month
    }

    fun setShowYearMonthPickerState(state: Boolean) {
        viewModelScope.launch {
            _showYearMonthPickerState.emit(state)
        }
    }

    fun setShowDiaryDeleteState(state: Boolean) {
        viewModelScope.launch {
            _showDiaryDeleteState.emit(state)
        }
    }

    fun setShowDiaryDeleteDialog(state: Boolean) {
        viewModelScope.launch {
            _showDiaryDeleteDialog.emit(state)
        }
    }

    fun updateSelectedYearMonth(year: Int, month: Int) {
        _selectedYearInCalendar.value = year
        _selectedMonthInCalendar.value = month
        isCalendarDataLoaded = false
    }
}
