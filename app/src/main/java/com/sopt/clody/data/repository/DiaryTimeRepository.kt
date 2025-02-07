package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto

interface DiaryTimeRepository {
    suspend fun getDiaryTime(year: Int, month: Int, date: Int): Result<DiaryTimeResponseDto>
}
