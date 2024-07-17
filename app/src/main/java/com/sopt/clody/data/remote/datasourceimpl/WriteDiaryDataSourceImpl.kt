package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.WriteDiaryService
import com.sopt.clody.data.remote.datasource.WriteDiaryDataSource
import com.sopt.clody.data.remote.dto.RequestWriteDiaryDto
import com.sopt.clody.data.remote.dto.ResponseWriteDiaryDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class WriteDiaryDataSourceImpl @Inject constructor(
    private val writeDiaryService: WriteDiaryService
) : WriteDiaryDataSource {
    override suspend fun writeDiary(date: String, content: List<String>): ApiResponse<ResponseWriteDiaryDto> {
        return writeDiaryService.writeDiary(RequestWriteDiaryDto(date, content))
    }
}
