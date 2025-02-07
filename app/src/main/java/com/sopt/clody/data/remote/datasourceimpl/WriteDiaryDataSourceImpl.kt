package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.datasource.WriteDiaryDataSource
import com.sopt.clody.data.remote.dto.request.WriteDiaryRequestDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class WriteDiaryDataSourceImpl @Inject constructor(
    private val writeDiaryService: WriteDiaryService
) : WriteDiaryDataSource {
    override suspend fun writeDiary(date: String, content: List<String>): ApiResponse<WriteDiaryResponseDto> {
        return writeDiaryService.writeDiary(WriteDiaryRequestDto(date, content))
    }
}
