package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.diarylist.ResponseMonthlyDiaryDto

interface DiaryListRepository {
    suspend fun getMonthlyDiary(
        year: Int,
        month: Int
    ): Result<ResponseMonthlyDiaryDto>
}
