package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.ResponseWriteDiaryDto

interface WriteDiaryRepository {
    suspend fun writeDiary(date: String, content: List<String>): Result<ResponseWriteDiaryDto>
}
