package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.data.remote.dto.diarylist.ResponseMonthlyDiaryDto
import com.sopt.clody.data.repository.DiaryListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Result


@HiltViewModel
class DiaryListViewModel
@Inject constructor(
    private val diaryListRepository: DiaryListRepository
): ViewModel() {
    private val _monthlyDiaryDto = MutableStateFlow<Result<ResponseMonthlyDiaryDto>?>(null)
    val monthlyDiaryDto: StateFlow<Result<ResponseMonthlyDiaryDto>?> get()= _monthlyDiaryDto

    fun fetchMonthlyDiary(year: Int, month: Int) {
        viewModelScope.launch {
            val result = diaryListRepository?.getMonthlyDiary(year, month)
            _monthlyDiaryDto.value = result
        }
    }
}
