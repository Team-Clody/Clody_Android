package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.ReplyDiaryService
import com.sopt.clody.data.remote.datasource.ReplyDiaryDataSource
import com.sopt.clody.data.remote.dto.ResponseReplyDiaryDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class ReplyDiaryDataSourceImpl @Inject constructor(
    private val replyDiaryService: ReplyDiaryService
) : ReplyDiaryDataSource {
    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): ApiResponse<ResponseReplyDiaryDto> {
        return replyDiaryService.getReplyDiary(year, month, date)
    }
}
