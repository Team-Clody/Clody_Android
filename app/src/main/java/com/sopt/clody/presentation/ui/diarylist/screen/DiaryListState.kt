package com.sopt.clody.presentation.ui.diarylist.screen

import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto

sealed class DiaryListState {
    data object Idle : DiaryListState()
    data object Loading : DiaryListState()
    data class Success(val data: MonthlyDiaryResponseDto) : DiaryListState()
    data class Failure(val errorMessage: String) : DiaryListState()
}
