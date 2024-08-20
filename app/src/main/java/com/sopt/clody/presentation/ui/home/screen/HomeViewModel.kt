package com.sopt.clody.presentation.ui.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.DailyDiariesRepository
import com.sopt.clody.data.repository.DailyDiaryListRepository
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import com.sopt.clody.domain.model.DiaryDateData
import com.sopt.clody.presentation.ui.diarylist.screen.DiaryDeleteState
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

    private val _calendarState = MutableStateFlow<CalendarState<MonthlyCalendarResponseDto>>(CalendarState.Idle)
    val calendarState: StateFlow<CalendarState<MonthlyCalendarResponseDto>> get() = _calendarState

    private val _dailyDiariesState = MutableStateFlow<DailyDiariesState<DailyDiariesResponseDto>>(DailyDiariesState.Idle)
    val dailyDiariesState: StateFlow<DailyDiariesState<DailyDiariesResponseDto>> get() = _dailyDiariesState

    private val _deleteDiaryState = MutableStateFlow<DeleteDiaryState>(DeleteDiaryState.Idle)
    val deleteDiaryState: StateFlow<DeleteDiaryState> get() = _deleteDiaryState

    private val _selectedDiaryDate = MutableStateFlow(DiaryDateData())
    val selectedDiaryDate: StateFlow<DiaryDateData> get() = _selectedDiaryDate

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> get() = _selectedDate

    private val _diaryCount = MutableStateFlow(0)
    val diaryCount: StateFlow<Int> get() = _diaryCount

    private val _replyStatus = MutableStateFlow("UNREADY")
    val replyStatus: StateFlow<String> get() = _replyStatus

    private val _isToday = MutableStateFlow(false)
    val isToday: StateFlow<Boolean> get() = _isToday

    private val _showYearMonthPickerState = MutableStateFlow(false)
    val showYearMonthPickerState: StateFlow<Boolean> get() = _showYearMonthPickerState

    private val _showDiaryDeleteState = MutableStateFlow(false)
    val showDiaryDeleteState: StateFlow<Boolean> get() = _showDiaryDeleteState

    private val _showDiaryDeleteDialog = MutableStateFlow(false)
    val showDiaryDeleteDialog: StateFlow<Boolean> get() = _showDiaryDeleteDialog

    private var isInitialized = false

    init {
        initialize()
    }

    private fun initialize() {
        if (!isInitialized) {
            val now = LocalDate.now()
            _selectedDiaryDate.value = DiaryDateData(now.year, now.monthValue)
            isInitialized = true
        }
    }

    fun loadCalendarData(year: Int, month: Int) {
        viewModelScope.launch {
            _calendarState.value = CalendarState.Loading
            val result = calendarRepository.getMonthlyCalendarData(year, month)
            _calendarState.value = result.fold(
                onSuccess = { CalendarState.Success(it) },
                onFailure = { CalendarState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun loadDailyDiariesData(year: Int, month: Int, date: Int) {
        viewModelScope.launch {
            _dailyDiariesState.value = DailyDiariesState.Loading
            val result = diariesRepository.getDailyDiariesData(year, month, date)
            _dailyDiariesState.value = result.fold(
                onSuccess = { DailyDiariesState.Success(it) },
                onFailure = { DailyDiariesState.Error(it.message ?: "Unknown error") }
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

    fun refreshCalendarDataCalendarData(year: Int, month: Int) {
        if (calendarState.value is CalendarState.Success && _selectedDiaryDate.value.year == year && _selectedDiaryDate.value.month == month) {
            return
        }
        _selectedDiaryDate.value = DiaryDateData(year, month)
        loadCalendarData(year, month)
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        loadDailyDiariesData(date.year, date.monthValue, date.dayOfMonth)
    }

    fun updateSelectedDiaryDate(diaryDate: DiaryDateData) {
        _selectedDiaryDate.value = diaryDate
    }

    fun updateDiaryState(diaries: List<MonthlyCalendarResponseDto.Diary>) {
        val selectedDiary = diaries.getOrNull(_selectedDate.value.dayOfMonth - 1)
        _diaryCount.value = selectedDiary?.diaryCount ?: 0
        _replyStatus.value = selectedDiary?.replyStatus ?: "UNREADY"
    }

    fun setShowYearMonthPickerState(state: Boolean) {
        _showYearMonthPickerState.value = state
    }

    fun setShowDiaryDeleteState(state: Boolean) {
        _showDiaryDeleteState.value = state
    }

    fun setShowDiaryDeleteDialog(state: Boolean) {
        _showDiaryDeleteDialog.value = state
    }
}

