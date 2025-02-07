package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.WriteDiaryDataSource
import com.sopt.clody.data.remote.dto.ResponseWriteDiaryDto
import com.sopt.clody.data.repository.WriteDiaryRepository
import com.sopt.clody.data.remote.util.handleApiResponse
import javax.inject.Inject

class WriteDiaryRepositoryImpl @Inject constructor(
    private val writeDiaryDataSource: WriteDiaryDataSource
) : WriteDiaryRepository {
    override suspend fun writeDiary(date: String, content: List<String>): Result<ResponseWriteDiaryDto> {
        return runCatching {
            val response = writeDiaryDataSource.writeDiary(date, content)
            response.handleApiResponse().getOrThrow()
        }
    }
}


