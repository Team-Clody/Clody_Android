package com.sopt.clody.presentation.ui.home.screen

import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.sopt.clody.core.fcm.FcmTokenProvider
import com.sopt.clody.core.network.NetworkConnectivityObserver
import com.sopt.clody.core.network.NetworkStatus
import com.sopt.clody.data.remote.dto.request.SendNotificationRequestDto
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.data.remote.dto.response.SendNotificationResponseDto
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.model.DailyDiaryInfo
import com.sopt.clody.domain.repository.DiaryRepository
import com.sopt.clody.domain.repository.DraftRepository
import com.sopt.clody.domain.repository.NotificationRepository
import com.sopt.clody.domain.repository.ReviewRepository
import com.sopt.clody.presentation.utils.base.UiLoadState
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
            is HomeContract.HomeIntent.InitializeInfo -> loadCalendarMonthlyInfo(intent.year, intent.month, intent.dayOfMonth)

            is HomeContract.HomeIntent.OnClickDiaryList -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToDiaryList)
            is HomeContract.HomeIntent.OnClickYearMonth -> setState { copy(showYearMonthPicker = true) }
            is HomeContract.HomeIntent.ConfirmYearMonthPicker -> loadCalendarMonthlyInfo(intent.newYear, intent.newMonth)
            is HomeContract.HomeIntent.DismissYearMonthPicker -> setState { copy(showYearMonthPicker = false) }
            is HomeContract.HomeIntent.OnClickSetting -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToSetting)
            is HomeContract.HomeIntent.OnClickDay -> loadDailyDiaryInfo(intent.year, intent.month, intent.dayOfMonth)
            is HomeContract.HomeIntent.OnClickDiaryDelete -> setState { copy(showDiaryDeleteBottomSheet = true) }
            is HomeContract.HomeIntent.ShowDiaryDeleteDialog -> setState { copy(showDiaryDeleteBottomSheet = false, showDiaryDeleteDialog = true) }
            is HomeContract.HomeIntent.ConfirmDiaryDelete -> deleteDiary(intent.year, intent.month, intent.dayOfMonth)
            is HomeContract.HomeIntent.DismissDiaryDelete -> setState { copy(showDiaryDeleteBottomSheet = false, showDiaryDeleteDialog = false) }
            is HomeContract.HomeIntent.OnClickWriteDiary -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToWriteDiary)
            is HomeContract.HomeIntent.OnClickReplyDiary -> _sideEffects.send(HomeContract.HomeSideEffect.NavigateToReplyLoading(intent.replyStatus))
            is HomeContract.HomeIntent.ShowDraftExpiredDialog -> setState { copy(showDraftExpiredDialog = true) }
            is HomeContract.HomeIntent.ConfirmDraftExpiredDialog -> {
                _sideEffects.send(HomeContract.HomeSideEffect.NavigateToWriteDiary)
                setState { copy(showDraftExpiredDialog = false) }
            }
            is HomeContract.HomeIntent.DismissDraftExpiredDialog -> setState { copy(showDraftExpiredDialog = false) }

            is HomeContract.HomeIntent.RequestNotificationPermission -> sendNotification(intent.granted)
            is HomeContract.HomeIntent.UpdateInAppReviewFlag -> updateInAppReviewFlag(intent.flag)
            is HomeContract.HomeIntent.EnableDraftAlarm -> enableDraftAlarm()
            is HomeContract.HomeIntent.UpdateDraftPopupFlag -> updateDraftPopupFlag(intent.show)
            is HomeContract.HomeIntent.DismissDraftNotificationToast -> setState { copy(showDraftNotificationToast = false) }

        }
    }

    private suspend fun loadCalendarMonthlyInfo(year: Int, month: Int, dayOfMonth: Int = 1) {
        setState { copy(calendarLoadState = UiLoadState.Loading, year = year, month = month) }

        if (networkConnectivityObserver.networkStatus.first() != NetworkStatus.Available) {
            setState { copy(errorMessage = errorMessageProvider.getNetworkError()) }
            return
        }

        withContext(Dispatchers.IO) { diaryRepository.getMonthlyCalendarData(year, month) }.fold(
            onSuccess = { data ->
                setState {
                    copy(
                        calendarLoadState = UiLoadState.Success,
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
                loadDailyDiaryInfo(year, month, dayOfMonth)
            },
            onFailure = {
                setState {
                    copy(
                        calendarLoadState = UiLoadState.Error,
                        errorMessage = errorMessageProvider.getServerError(),
                    )
                }
            },
        )
    }

    private suspend fun loadDailyDiaryInfo(year: Int, month: Int, dayOfMonth: Int) {
        setState { copy(dailyDiaryLoadState = UiLoadState.Loading, dayOfMonth = dayOfMonth) }

        if (networkConnectivityObserver.networkStatus.first() != NetworkStatus.Available) {
            setState { copy(errorMessage = errorMessageProvider.getNetworkError()) }
            return
        }

        withContext(Dispatchers.IO) { diaryRepository.getDailyDiariesData(year, month, dayOfMonth) }.fold(
            onSuccess = { data ->
                setState {
                    copy(
                        dailyDiaryLoadState = UiLoadState.Success,
                        dailyDiaryInfo = DailyDiaryInfo(
                            diaryList = data.diaries.map { it.content },
                            isDraft = data.isDraft,
                        ),
                    )
                }
            },
            onFailure = {
                setState {
                    copy(
                        dailyDiaryLoadState = UiLoadState.Error,
                        errorMessage = errorMessageProvider.getServerError(),
                    )
                }
            },
        )
    }

    private suspend fun deleteDiary(year: Int, month: Int, dayOfMonth: Int) {
        setState { copy(diaryDeleteState = UiLoadState.Loading) }

        if (networkConnectivityObserver.networkStatus.first() != NetworkStatus.Available) {
            setState { copy(errorMessage = errorMessageProvider.getNetworkError()) }
            return
        }

        withContext(Dispatchers.IO) { diaryRepository.deleteDailyDiary(year, month, dayOfMonth) }.fold(
            onSuccess = {
                loadCalendarMonthlyInfo(year, month, dayOfMonth)
                setState { copy(showDiaryDeleteDialog = false, diaryDeleteState = UiLoadState.Success) }
            },
            onFailure = {
                setState { copy(diaryDeleteState = UiLoadState.Error, errorMessage = errorMessageProvider.getServerError()) }
            },
        )
    }

    // 공통 유틸: 알림 설정(info) 조회 실패 시 에러 상태 세팅 후 null 반환
    private suspend fun getNotificationInfoOrNull(): NotificationInfoResponseDto? =
        withContext(Dispatchers.IO) { notificationRepository.getNotificationInfo() }
            .getOrElse {
                setState { copy(errorMessage = errorMessageProvider.getTemporaryError()) }
                null
            }

    // 공통 유틸: 요청 생성 람다만 넘기면 전송까지 수행
    private suspend fun buildAndSendNotification(
        build: (info: NotificationInfoResponseDto, fcmToken: String) -> SendNotificationRequestDto
    ): Result<SendNotificationResponseDto> {
        val token = fcmTokenProvider.getToken().orEmpty()
        val info = getNotificationInfoOrNull() ?: return Result.failure(IllegalStateException("notification info null"))
        val request = build(info, token)
        return withContext(Dispatchers.IO) { notificationRepository.sendNotification(request) }
    }

    // 권한 부여/해제에 따른 전송 (결과는 기존처럼 별도 처리 없이 호출만)
    private suspend fun sendNotification(granted: Boolean) {
        buildAndSendNotification { info, token ->
            SendNotificationRequestDto(
                isDiaryAlarm = granted,
                isDraftAlarm = info.isDraftAlarm,
                isReplyAlarm = granted,
                time = info.time,
                fcmToken = token,
            )
        }
    }

    // 임시저장(draft) 알림 활성화
    private suspend fun enableDraftAlarm() {
        if (networkConnectivityObserver.networkStatus.first() != NetworkStatus.Available) {
            setState { copy(errorMessage = errorMessageProvider.getNetworkError()) }
            return
        }

        buildAndSendNotification { info, token ->
            SendNotificationRequestDto(
                isDiaryAlarm = info.isDiaryAlarm,
                isDraftAlarm = true,
                isReplyAlarm = info.isReplyAlarm,
                time = info.time,
                fcmToken = token,
            )
        }.fold(
            onSuccess = {
                setState { copy(showDraftNotificationPopup = false, showDraftNotificationToast = true) }
            },
            onFailure = {
                setState { copy(errorMessage = errorMessageProvider.getTemporaryError()) }
            },
        )
    }

    private fun updateDraftPopupFlag(flag: Boolean) {
        draftRepository.setIsFirstUse(flag)
        setState { copy(showDraftNotificationPopup = flag) }
    }

    private fun updateInAppReviewFlag(flag: Boolean) {
        reviewRepository.setShouldShowPopup(flag)
        setState { copy(showInAppReviewPopup = flag) }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<HomeViewModel, HomeContract.HomeState> {
        override fun create(state: HomeContract.HomeState): HomeViewModel
    }

    companion object :
        MavericksViewModelFactory<HomeViewModel, HomeContract.HomeState> by hiltMavericksViewModelFactory()
}
