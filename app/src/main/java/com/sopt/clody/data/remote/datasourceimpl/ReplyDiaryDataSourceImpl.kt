package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.datasource.ReplyDiaryDataSource
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class ReplyDiaryDataSourceImpl @Inject constructor(
    private val replyDiaryService: ReplyDiaryService
) : ReplyDiaryDataSource {
    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): ApiResponse<ReplyDiaryResponseDto> {
        return replyDiaryService.getReplyDiary(year, month, date)
    }
}
