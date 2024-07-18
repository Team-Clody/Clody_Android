package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.ResponseReplyDiaryDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface ReplyDiaryDataSource {
    suspend fun getReplyDiary(year: Int, month: Int, date: Int): ApiResponse<ResponseReplyDiaryDto>
}
