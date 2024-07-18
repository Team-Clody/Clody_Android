package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.ResponseReplyDiaryDto

interface ReplyDiaryRepository {
    suspend fun getReplyDiary(year: Int, month: Int, date: Int): Result<ResponseReplyDiaryDto>
}
