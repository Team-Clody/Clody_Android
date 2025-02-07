package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto

interface ReplyDiaryRepository {
    suspend fun getReplyDiary(year: Int, month: Int, date: Int): Result<ReplyDiaryResponseDto>
}
