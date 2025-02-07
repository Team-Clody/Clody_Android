package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.presentation.utils.extension.getDayOfWeek
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_NETWORK_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import com.sopt.clody.presentation.utils.network.ErrorMessages.UNKNOWN_ERROR
import com.sopt.clody.data.remote.util.NetworkUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val diaryListRepository: DiaryListRepository,
    private val dailyDiaryListRepository: DailyDiaryListRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

    private val _diaryListState = MutableStateFlow<DiaryListState>(DiaryListState.Idle)
    val diaryListState: StateFlow<DiaryListState> = _diaryListState

    private val _selectedDiaryDate = MutableStateFlow(DiaryDate())
    val selectedDiaryDate: StateFlow<DiaryDate> = _selectedDiaryDate

    private val _diaryDeleteState = MutableStateFlow<DiaryDeleteState>(DiaryDeleteState.Idle)
    val diaryDeleteState: StateFlow<DiaryDeleteState>  = _diaryDeleteState

    private val _showDiaryDeleteFailureDialog = MutableStateFlow(false)
    val showDiaryDeleteFailureDialog: StateFlow<Boolean> = _showDiaryDeleteFailureDialog

    private val _failureDialogMessage = MutableStateFlow("")
    val failureDialogMessage: StateFlow<String> = _failureDialogMessage

    private val maxRetryCount = 3
    private var retryCount = 0

    fun fetchMonthlyDiary(year: Int, month: Int) {
        if (retryCount >= maxRetryCount) return
        _diaryListState.value = DiaryListState.Loading
        viewModelScope.launch {
            if (!networkUtil.isNetworkAvailable()) {
                _diaryListState.value = DiaryListState.Failure(FAILURE_NETWORK_MESSAGE)
                return@launch
            }
            val result = diaryListRepository.getMonthlyDiary(year, month)
            _diaryListState.value = result.fold(
                onSuccess = {
                    retryCount = 0
                    DiaryListState.Success(it)
                },
                onFailure = {
                    retryCount++
                    if (retryCount >= maxRetryCount) {
                        DiaryListState.Failure(FAILURE_TEMPORARY_MESSAGE)
                    } else {
                        val errorMessage = if(it.message?.contains("200") == false) {
                            FAILURE_TEMPORARY_MESSAGE
                        } else { it.localizedMessage ?: UNKNOWN_ERROR }
                        DiaryListState.Failure(errorMessage)
                    }
                }
            )
        }
    }

    fun setSelectedDiaryDate(selectedDiaryDate: String) {
        val diaryDate = selectedDiaryDate.split("-")
        val year = diaryDate[0].toInt()
        val month = diaryDate[1].toInt()
        val day = diaryDate[2].toInt()
        val dayOfWeek = getDayOfWeek(year, month, day)
        _selectedDiaryDate.value = DiaryDate(year, month, day, dayOfWeek)
    }

    fun deleteDailyDiary(year: Int, month: Int, day: Int) {
        _diaryDeleteState.value = DiaryDeleteState.Loading
        viewModelScope.launch {
            if(!networkUtil.isNetworkAvailable()) {
                _failureDialogMessage.value = FAILURE_NETWORK_MESSAGE
                DiaryDeleteState.Failure(_failureDialogMessage.value)
                _showDiaryDeleteFailureDialog.value = true
                return@launch
            }
            val result = dailyDiaryListRepository.deleteDailyDiary(year, month, day)
            _diaryDeleteState.value = result.fold(
                onSuccess = {
                    DiaryDeleteState.Success
                },
                onFailure = {
                    _failureDialogMessage.value = if (it.message?.contains("200") == false) {
                        FAILURE_TEMPORARY_MESSAGE
                    } else {
                        it.localizedMessage ?: UNKNOWN_ERROR
                    }
                    _showDiaryDeleteFailureDialog.value = true
                    DiaryDeleteState.Failure(_failureDialogMessage.value)
                }
            )
        }
    }

    fun dismissDiaryDeleteFailureDialog() {
        _showDiaryDeleteFailureDialog.value = false
        _failureDialogMessage.value = ""
    }

    data class DiaryDate(
        val year: Int = 0,
        val month: Int = 0,
        val day: Int = 0,
        val dayOfWeek: String = ""
    )
}
