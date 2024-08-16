package com.sopt.clody.presentation.ui.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.repository.DailyDiariesRepository
import com.sopt.clody.data.repository.DailyDiaryListRepository
import com.sopt.clody.data.repository.MonthlyCalendarRepository
import com.sopt.clody.domain.model.DiaryDateData
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

    private val _loadedCalendarDate = MutableStateFlow<Pair<Int, Int>?>(null)
    val loadedCalendarDate: StateFlow<Pair<Int, Int>?> = _loadedCalendarDate

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

    private val _selectedDiaryDate = MutableStateFlow(DiaryDateData())
    val selectedDiaryDate: StateFlow<DiaryDateData> get() = _selectedDiaryDate

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> get() = _selectedDate

    private val _showYearMonthPickerState = MutableStateFlow(false)
    val showYearMonthPickerState: StateFlow<Boolean> get() = _showYearMonthPickerState

    private val _showDiaryDeleteState = MutableStateFlow(false)
    val showDiaryDeleteState: StateFlow<Boolean> get() = _showDiaryDeleteState

    private val _showDiaryDeleteDialog = MutableStateFlow(false)
    val showDiaryDeleteDialog: StateFlow<Boolean> get() = _showDiaryDeleteDialog

    // 상태 플래그
     var isCalendarDataLoadedFlag = false
     var isDailyDiariesDataLoaded = false

    var isInitialized = false

    init {
        initialize()
    }

    private fun initialize() {
        if (!isInitialized) {
            val now = LocalDate.now()
            updateIsToday(now.year, now.monthValue)
            loadCalendarData(now.year, now.monthValue)
            updateSelectedDate(now)
            isInitialized = true
        }
    }

    fun shouldUpdateCalendar(year: Int, month: Int): Boolean {
        return loadedCalendarDate.value != Pair(year, month)
    }

    fun loadCalendarData(year: Int, month: Int) {
        if (isCalendarDataLoadedFlag && !shouldUpdateCalendar(year, month)) return

        viewModelScope.launch {
            _calendarUiState.value = CalendarState.Loading
            val result = calendarRepository.getMonthlyCalendarData(year, month)
            _calendarUiState.value = result.fold(
                onSuccess = { data ->
                    updateDiaryState(data.diaries)
                    _loadedCalendarDate.value = Pair(year, month)
                    isCalendarDataLoadedFlag = true
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
                    refreshDataAfterDelete(year, month, day)
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
        _showYearMonthPickerState.value = state
    }

    fun setShowDiaryDeleteState(state: Boolean) {
        _showDiaryDeleteState.value = state
    }

    fun setShowDiaryDeleteDialog(state: Boolean) {
        _showDiaryDeleteDialog.value = state
    }

    fun updateSelectedDiaryDate(diaryDate: DiaryDateData) {
        _selectedDiaryDate.value = diaryDate
        isCalendarDataLoadedFlag = false
    }

    fun refreshCalendarAndDiaries(year: Int, month: Int, date: LocalDate) {
        loadCalendarData(year, month)
        loadDailyDiariesData(date.year, date.monthValue, date.dayOfMonth)
    }

    private fun resetDataLoadFlags() {
        isCalendarDataLoadedFlag = false
        isDailyDiariesDataLoaded = false
    }

    private fun refreshDataAfterDelete(year: Int, month: Int, day: Int) {
        loadCalendarData(year, month)
        loadDailyDiariesData(year, month, day)

        if (_selectedDate.value.year == year && _selectedDate.value.monthValue == month && _selectedDate.value.dayOfMonth == day) {
            isDailyDiariesDataLoaded = false
            loadDailyDiariesData(year, month, day)
        }
    }
}
