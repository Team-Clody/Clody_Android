package com.sopt.clody.presentation.ui.home.screen

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.sopt.clody.core.fcm.FcmTokenProvider
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.model.DailyDiaryInfo
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.domain.repository.DraftRepository
import com.sopt.clody.domain.repository.NotificationRepository
import com.sopt.clody.domain.repository.ReviewRepository
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel @AssistedInject constructor(
    @Assisted initialState: HomeContract.HomeState,
    private val diaryRepository: DiaryRepository,
    private val notificationRepository: NotificationRepository,
    private val draftRepository: DraftRepository,
    private val fcmTokenProvider: FcmTokenProvider,
    private val reviewRepository: ReviewRepository,
    private val errorMessageProvider: ErrorMessageProvider,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
) : MavericksViewModel<HomeContract.HomeState>(initialState) {

    private val _intents = Channel<HomeContract.HomeIntent>(BUFFERED)
    private val _sideEffects = Channel<HomeContract.HomeSideEffect>(BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _intents
            .receiveAsFlow()
            .onEach(::handleIntent)
            .launchIn(viewModelScope)
    }

    fun postIntent(intent: HomeContract.HomeIntent) {
        viewModelScope.launch { _intents.send(intent) }
    }

    private suspend fun handleIntent(intent: HomeContract.HomeIntent) {
        when (intent) {
            is HomeContract.HomeIntent.LoadCalendarMonthlyInfo -> loadCalendarMonthlyInfo(intent.year, intent.month)
            is HomeContract.HomeIntent.LoadDailyDiaryInfo -> loadDailyDiaryInfo(intent.year, intent.month, intent.dayOfMonth)
            is HomeContract.HomeIntent.OnClickDiaryList -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToDiaryList)
            is HomeContract.HomeIntent.OnClickYearMonth -> setState { copy(showYearMonthPicker = true) }
            is HomeContract.HomeIntent.UpdateYearMonth -> updateYearMonth(intent.newYear, intent.newMonth)
            is HomeContract.HomeIntent.DismissYearMonthPicker -> setState { copy(showYearMonthPicker = false) }
            is HomeContract.HomeIntent.OnClickSetting -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToSetting)

            is HomeContract.HomeIntent.OnClickDiaryDelete -> setState { copy(showDiaryDeleteBottomSheet = true) }
            is HomeContract.HomeIntent.ShowDiaryDeleteDialog -> setState { copy(showDiaryDeleteDialog = true, showDiaryDeleteBottomSheet = false) }
            is HomeContract.HomeIntent.ConfirmDiaryDelete -> deleteDiary(intent.year, intent.month, intent.dayOfMonth)
            is HomeContract.HomeIntent.DismissDiaryDelete -> setState { copy(showDiaryDeleteDialog = false) }

            is HomeContract.HomeIntent.OnClickWriteDiary -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToWriteDiary)
            is HomeContract.HomeIntent.ShowDraftExpiredDialog -> setState { copy(showDraftExpiredDialog = true) }

            is HomeContract.HomeIntent.OnClickReplyDiary -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToReplyLoading)

            is HomeContract.HomeIntent.EnableDraftAlarm -> enableDraftAlarm()
            is HomeContract.HomeIntent.SendNotification -> sendNotification(intent.granted)
            is HomeContract.HomeIntent.UpdateInAppReview -> updateInAppReview(intent.show)
            is HomeContract.HomeIntent.UpdateDraftPopup -> updateDraftPopup(intent.show)
        }
    }

    private suspend fun isNetworkAvailable(): Boolean =
        networkConnectivityObserver.networkStatus.first() == NetworkStatus.Available

    private suspend fun loadCalendarMonthlyInfo(year: Int, month: Int) {
        setState { copy(homeUiState = HomeUiState.Loading, year = year, month = month, dayOfMonth = 1) }

        if (!isNetworkAvailable()) {
            setState { copy(errorMessage = errorMessageProvider.getNetworkError()) }
            return
        }

        val result = withContext(Dispatchers.IO) {
            diaryRepository.getMonthlyCalendarData(year, month)
        }

        result.fold(
            onSuccess = { data ->
                setState {
                    copy(
                        homeUiState = HomeUiState.Success,
                        calendarMonthlyInfo = CalendarMonthlyInfo(
                            totalCloverCount = data.totalCloverCount,
                            calendarDailyInfoList = data.diaries.map {
                                CalendarMonthlyInfo.CalendarDailyInfo(
                                    diaryCount = it.diaryCount,
                                    replyStatus = it.replyStatus,
                                    date = it.date,
                                    isDeleted = it.isDeleted,
                                )
                            },
                        ),
                    )
                }
                // 월간 데이터 로드 완료 후 일간 데이터 로드
                loadDailyDiaryInfo(year, month, 1)
            },
            onFailure = {
                setState {
                    copy(
                        homeUiState = HomeUiState.Error,
                        errorMessage = errorMessageProvider.getTemporaryError(),
                    )
                }
            },
        )
    }

    private suspend fun loadDailyDiaryInfo(year: Int, month: Int, day: Int) {
        setState { copy(homeUiState = HomeUiState.Loading, dayOfMonth = day) }

        if (!isNetworkAvailable()) {
            setState { copy(errorMessage = errorMessageProvider.getNetworkError()) }
            return
        }

        val result = withContext(Dispatchers.IO) {
            diaryRepository.getDailyDiariesData(year, month, day)
        }

        result.fold(
            onSuccess = { data ->
                setState {
                    copy(
                        homeUiState = HomeUiState.Success,
                        selectedDailyInfo = DailyDiaryInfo(
                            diaryList = data.diaries.map { it.content },
                            isDraft = data.isDraft,
                        ),
                    )
                }
            },
            onFailure = {
                setState {
                    copy(
                        homeUiState = HomeUiState.Error,
                        errorMessage = errorMessageProvider.getTemporaryError(),
                    )
                }
            },
        )
    }

    private suspend fun updateYearMonth(newYear: Int, newMonth: Int) {
        loadCalendarMonthlyInfo(newYear, newMonth)
    }

    private suspend fun deleteDiary(year: Int, month: Int, dayOfMonth: Int) {
        setState { copy(homeUiState = HomeUiState.Loading) }

        val result = withContext(Dispatchers.IO) {
            diaryRepository.deleteDailyDiary(year, month, dayOfMonth)
        }
        result.fold(
            onSuccess = {
                loadCalendarMonthlyInfo(year, month)
                setState {
                    copy(
                        homeUiState = HomeUiState.Success,
                        showDiaryDeleteDialog = false,
                        showDiaryDeleteBottomSheet = false,
                    )
                }
            },
            onFailure = {
                setState {
                    copy(
                        homeUiState = HomeUiState.Error,
                        errorMessage = errorMessageProvider.getTemporaryError(),
                    )
                }
            },
        )
    }

    private suspend fun enableDraftAlarm() {
        setState { copy(homeUiState = HomeUiState.Loading) }

        if (!isNetworkAvailable()) {
            setState { copy(errorMessage = errorMessageProvider.getNetworkError()) }
            return
        }

        val fcmToken = fcmTokenProvider.getToken().orEmpty()
        val info = withContext(Dispatchers.IO) { notificationRepository.getNotificationInfo() }
            .getOrElse {
                setState { copy(errorMessage = errorMessageProvider.getTemporaryError()) }
                return
            }

        val request = com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto(
            isDiaryAlarm = info.isDiaryAlarm,
            isDraftAlarm = true,
            isReplyAlarm = info.isReplyAlarm,
            time = info.time,
            fcmToken = fcmToken,
        )

        withContext(Dispatchers.IO) { notificationRepository.sendNotification(request) }
            .fold(
                onSuccess = {
                    setState {
                        copy(
                            homeUiState = HomeUiState.Success,
                            showDraftNotificationToast = true,
                            showDraftNotificationPopup = false,
                        )
                    }
                },
                onFailure = {
                    setState {
                        copy(
                            homeUiState = HomeUiState.Error,
                            errorMessage = errorMessageProvider.getTemporaryError(),
                        )
                    }
                },
            )
    }

    private suspend fun sendNotification(granted: Boolean) {
        setState { copy(homeUiState = HomeUiState.Loading) }

        val fcmToken = fcmTokenProvider.getToken().orEmpty()
        val info = withContext(Dispatchers.IO) { notificationRepository.getNotificationInfo() }
            .getOrElse { return }

        val request = com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto(
            isDiaryAlarm = granted,
            isDraftAlarm = info.isDraftAlarm,
            isReplyAlarm = granted,
            time = info.time,
            fcmToken = fcmToken,
        )
        withContext(Dispatchers.IO) { notificationRepository.sendNotification(request) }
            .fold(
                onSuccess = { setState { copy(homeUiState = HomeUiState.Success) } },
                onFailure = { setState { copy(homeUiState = HomeUiState.Error) } },
            )
    }

    private fun updateInAppReview(show: Boolean) {
        reviewRepository.setShouldShowPopup(show)
        setState { copy(showInAppReviewPopup = show) }
    }

    private fun updateDraftPopup(show: Boolean) {
        if (!show) draftRepository.setIsFirstUse(false)
        setState { copy(showDraftNotificationPopup = show) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<HomeViewModel, HomeContract.HomeState> {
        override fun create(state: HomeContract.HomeState): HomeViewModel
    }

    companion object :
        MavericksViewModelFactory<HomeViewModel, HomeContract.HomeState> by hiltMavericksViewModelFactory()
}
