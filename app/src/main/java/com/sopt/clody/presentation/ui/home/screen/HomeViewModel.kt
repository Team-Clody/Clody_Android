package com.sopt.clody.presentation.ui.home.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.core.fcm.FcmTokenProvider
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.domain.model.ReplyStatus
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.domain.repository.DraftRepository
import com.sopt.clody.domain.repository.NotificationRepository
import com.sopt.clody.domain.repository.ReviewRepository
import com.sopt.clody.presentation.ui.home.calendar.model.DiaryDateData
import com.sopt.clody.presentation.ui.setting.notificationsetting.screen.NotificationChangeState
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    private val notificationRepository: NotificationRepository,
    private val draftRepository: DraftRepository,
    private val fcmTokenProvider: FcmTokenProvider,
    private val reviewRepository: ReviewRepository,
    private val errorMessageProvider: ErrorMessageProvider,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {

    private val _calendarState = MutableStateFlow<CalendarState<MonthlyCalendarResponseDto>>(CalendarState.Idle)
    val calendarState: StateFlow<CalendarState<MonthlyCalendarResponseDto>> get() = _calendarState

    private val _dailyDiariesState =
        MutableStateFlow<DailyDiariesState<DailyDiariesResponseDto>>(DailyDiariesState.Idle)
    val dailyDiariesState: StateFlow<DailyDiariesState<DailyDiariesResponseDto>> get() = _dailyDiariesState

    private val _deleteDiaryState = MutableStateFlow<DeleteDiaryState>(DeleteDiaryState.Idle)
    val deleteDiaryState: StateFlow<DeleteDiaryState> get() = _deleteDiaryState

    private val _deleteDiaryResult = MutableStateFlow<DeleteDiaryState>(DeleteDiaryState.Idle)
    val deleteDiaryResult: StateFlow<DeleteDiaryState> get() = _deleteDiaryResult

    private val _selectedDiaryDate = MutableStateFlow(DiaryDateData())
    val selectedDiaryDate: StateFlow<DiaryDateData> get() = _selectedDiaryDate

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> get() = _selectedDate

    private val _diaryCount = MutableStateFlow(0)
    val diaryCount: StateFlow<Int> get() = _diaryCount

    private val _replyStatus = MutableStateFlow(ReplyStatus.UNREADY)
    val replyStatus: StateFlow<ReplyStatus> get() = _replyStatus

    private val _isToday = MutableStateFlow(false)
    val isToday: StateFlow<Boolean> get() = _isToday

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> get() = _isDeleted

    private val _showYearMonthPickerState = MutableStateFlow(false)
    val showYearMonthPickerState: StateFlow<Boolean> get() = _showYearMonthPickerState

    private val _showDiaryDeleteState = MutableStateFlow(false)
    val showDiaryDeleteState: StateFlow<Boolean> get() = _showDiaryDeleteState

    private val _showDiaryDeleteDialog = MutableStateFlow(false)
    val showDiaryDeleteDialog: StateFlow<Boolean> get() = _showDiaryDeleteDialog

    private val _showContinueDraftDialog = MutableStateFlow(false)
    val showContinueDraftDialog: StateFlow<Boolean> get() = _showContinueDraftDialog

    private val _showFirstDraftPopup = MutableStateFlow(draftRepository.getIsFirstUse())
    val showFirstDraftPopup: StateFlow<Boolean> = _showFirstDraftPopup

    private val _draftAlarmChangeState =
        MutableStateFlow<NotificationChangeState>(NotificationChangeState.Idle)
    val draftAlarmChangeState: StateFlow<NotificationChangeState> = _draftAlarmChangeState

    private val _draftAlarmEnableToast = MutableStateFlow(false)
    val draftAlarmEnableToast: StateFlow<Boolean> = _draftAlarmEnableToast

    private val _showInAppReviewPopup = MutableStateFlow(reviewRepository.getShouldShowPopup())
    val showInAppReviewPopup: StateFlow<Boolean> get() = _showInAppReviewPopup

    private val _errorState = MutableStateFlow(false to "")
    val errorState: StateFlow<Pair<Boolean, String>> = _errorState

    private val _hasDraft = MutableStateFlow(false)
    val hasDraft: StateFlow<Boolean> get() = _hasDraft

    private val _isNotificationPermissionGranted = MutableStateFlow(false)

    private var isInitialized = false

    init {
        initialize()
    }

    private fun initialize() {
        if (!isInitialized) {
            val now = LocalDate.now()
            _selectedDiaryDate.value = DiaryDateData(now.year, now.monthValue)
            _selectedDate.value = now
            isInitialized = true
        }
    }

    fun setErrorState(isError: Boolean, message: String = errorMessageProvider.getTemporaryError()) {
        _errorState.value = isError to message
    }

    private suspend fun loadCalendarData(year: Int, month: Int) {
        if (!isNetworkAvailable()) {
            setErrorState(true, errorMessageProvider.getNetworkError())
            return
        }
        _calendarState.value = CalendarState.Loading
        val result = withContext(Dispatchers.IO) {
            diaryRepository.getMonthlyCalendarData(year, month)
        }
        _calendarState.value = result.fold(
            onSuccess = {
                setErrorState(false)
                CalendarState.Success(it)
            },
            onFailure = {
                setErrorState(true, errorMessageProvider.getTemporaryError())
                CalendarState.Error(errorMessageProvider.getTemporaryError())
            },
        )
    }

    private suspend fun loadDailyDiariesData(year: Int, month: Int, date: Int) {
        if (!isNetworkAvailable()) {
            setErrorState(true, errorMessageProvider.getNetworkError())
            return
        }
        _dailyDiariesState.value = DailyDiariesState.Loading
        val result = withContext(Dispatchers.IO) {
            diaryRepository.getDailyDiariesData(year, month, date)
        }
        _dailyDiariesState.value = result.fold(
            onSuccess = { dailyResponse ->
                _hasDraft.value = dailyResponse.isDraft
                _diaryCount.value = dailyResponse.diaries.size
                _isDeleted.value = dailyResponse.isDeleted
                setErrorState(false)
                DailyDiariesState.Success(dailyResponse)
            },
            onFailure = {
                setErrorState(true, errorMessageProvider.getTemporaryError())
                DailyDiariesState.Error(errorMessageProvider.getTemporaryError())
            },
        )
    }

    fun deleteDailyDiary(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _deleteDiaryResult.value = DeleteDiaryState.Loading
            val result = withContext(Dispatchers.IO) {
                diaryRepository.deleteDailyDiary(year, month, day)
            }
            _deleteDiaryResult.value = result.fold(
                onSuccess = {
                    loadCalendarData(year, month)
                    loadDailyDiariesData(year, month, day)
                    _diaryCount.value = 0
                    _isDeleted.value = false
                    _replyStatus.value = ReplyStatus.UNREADY
                    DeleteDiaryState.Success
                },
                onFailure = {
                    DeleteDiaryState.Failure(it.message ?: errorMessageProvider.getTemporaryError())
                },
            )
        }
    }

    private val loadDataMutex = Mutex()
    fun updateYearMonthAndLoadData(year: Int, month: Int) {
        viewModelScope.launch {
            loadDataMutex.withLock {
                val sameYm = _selectedDiaryDate.value.year == year &&
                    _selectedDiaryDate.value.month == month

                val calendarLoaded = calendarState.value is CalendarState.Success
                val dailyLoaded = dailyDiariesState.value is DailyDiariesState.Success
                val selectedIsFirst = _selectedDate.value.dayOfMonth == 1
                val alreadyLoaded = sameYm && calendarLoaded && (selectedIsFirst && dailyLoaded)

                if (alreadyLoaded) return@withLock

                _selectedDiaryDate.value = DiaryDateData(year, month)
                _selectedDate.value = LocalDate.of(year, month, 1)

                coroutineScope {
                    awaitAll(
                        async { loadCalendarData(year, month) },
                        async { loadDailyDiariesData(year, month, 1) },
                    )
                }
            }
        }
    }

    fun updateYearMonthAndLoadData(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            loadDataMutex.withLock {
                val sameYmd = _selectedDiaryDate.value.year == year &&
                    _selectedDiaryDate.value.month == month &&
                    _selectedDate.value.dayOfMonth == day

                val calendarLoaded = calendarState.value is CalendarState.Success
                val dailyLoaded = dailyDiariesState.value is DailyDiariesState.Success
                val alreadyLoaded = sameYmd && calendarLoaded && dailyLoaded

                if (alreadyLoaded) return@withLock

                _selectedDiaryDate.value = DiaryDateData(year, month)
                _selectedDate.value = LocalDate.of(year, month, day)

                coroutineScope {
                    awaitAll(
                        async { loadCalendarData(year, month) },
                        async { loadDailyDiariesData(year, month, day) },
                    )
                }
            }
        }
    }

    fun updateSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        viewModelScope.launch {
            loadDailyDiariesData(date.year, date.monthValue, date.dayOfMonth)
        }
    }

    fun updateDiaryState(diaries: List<MonthlyCalendarResponseDto.Diary>) {
        val selectedDiary = diaries.getOrNull(_selectedDate.value.dayOfMonth - 1)
        _diaryCount.value = selectedDiary?.diaryCount ?: 0
        _replyStatus.value = selectedDiary?.replyStatus ?: ReplyStatus.UNREADY
        _isDeleted.value = selectedDiary?.isDeleted ?: false
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

    fun setShowContinueDraftDialog(state: Boolean) {
        _showContinueDraftDialog.value = state
    }

    fun updateFirstDraftUse(newState: Boolean) {
        draftRepository.setIsFirstUse(false)
        _showFirstDraftPopup.value = newState
    }

    fun canWriteDiary(): Boolean {
        val userTimeZone = ZoneId.systemDefault().id
        val today = LocalDate.now()
        val selected = _selectedDate.value
        val isAvailableDay = if (userTimeZone == "Asia/Seoul") {
            selected == today || selected == today.minusDays(1)
        } else {
            selected == today
        }
        return _diaryCount.value == 0 && isAvailableDay
    }

    fun canReplyDiary(): Boolean {
        return _diaryCount.value > 0 && !_isDeleted.value
    }

    fun isValidDraftDate(): Boolean {
        val today = LocalDate.now()
        val selected = _selectedDate.value
        return selected == today || selected == today.minusDays(1)
    }

    fun enableDraftAlarm() {
        viewModelScope.launch {
            if (!isNetworkAvailable()) {
                setErrorState(true, errorMessageProvider.getNetworkError())
                return@launch
            }

            val fcmToken = fcmTokenProvider.getToken().orEmpty()
            val notificationInfo = getNotificationInfo() ?: return@launch
            val request = buildDraftAlarmRequest(notificationInfo, fcmToken)
            sendDraftAlarmRequest(request)
        }
    }

    private suspend fun isNetworkAvailable(): Boolean {
        return networkConnectivityObserver.networkStatus.first() == NetworkStatus.Available
    }

    private suspend fun getNotificationInfo(): NotificationInfoResponseDto? {
        return notificationRepository.getNotificationInfo().getOrElse {
            _draftAlarmChangeState.value = NotificationChangeState.Failure(errorMessageProvider.getTemporaryError())
            null
        }
    }

    private fun buildDraftAlarmRequest(
        info: NotificationInfoResponseDto,
        fcmToken: String,
    ): SendNotificationRequestDto = SendNotificationRequestDto(
        isDiaryAlarm = info.isDiaryAlarm,
        isDraftAlarm = true,
        isReplyAlarm = info.isReplyAlarm,
        time = info.time,
        fcmToken = fcmToken,
    )

    private suspend fun sendDraftAlarmRequest(request: SendNotificationRequestDto) {
        withContext(Dispatchers.IO) {
            notificationRepository.sendNotification(request)
        }.fold(
            onSuccess = {
                _draftAlarmEnableToast.value = true
                _draftAlarmChangeState.value = NotificationChangeState.Success(it)
            },
            onFailure = {
                _draftAlarmChangeState.value =
                    NotificationChangeState.Failure(errorMessageProvider.getTemporaryError())
            },
        )
    }

    fun resetDraftAlarmEnableToast() {
        _draftAlarmEnableToast.value = false
    }

    fun updateShowInAppReviewPopup(state: Boolean) {
        reviewRepository.setShouldShowPopup(state)
        _showInAppReviewPopup.value = state
    }

    fun updateNotificationPermissionGranted(isGranted: Boolean) {
        _isNotificationPermissionGranted.value = isGranted
    }
}
