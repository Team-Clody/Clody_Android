package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.repository.ReplyDiaryRepository
import com.sopt.clody.presentation.utils.network.ErrorMessages.FAILURE_TEMPORARY_MESSAGE
import javax.inject.Inject

class ReplyDiaryRepositoryImpl @Inject constructor(
    private val replyDiaryDataSource: ReplyDiaryDataSource
) : ReplyDiaryRepository {
    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): Result<ReplyDiaryResponseDto> {
        return runCatching {
            val response = replyDiaryDataSource.getReplyDiary(year, month, date).data
            if (response.content == null) {
                throw IllegalStateException(FAILURE_TEMPORARY_MESSAGE)
            }
            response
        }
    }
}

