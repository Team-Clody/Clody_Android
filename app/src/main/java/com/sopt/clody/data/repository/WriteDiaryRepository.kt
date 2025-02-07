package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto

interface WriteDiaryRepository {
    suspend fun writeDiary(date: String, content: List<String>): Result<WriteDiaryResponseDto>
}
