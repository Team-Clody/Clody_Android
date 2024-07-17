package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.RequestWriteDiaryDto
import com.sopt.clody.data.remote.dto.ResponseWriteDiaryDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface WriteDiaryService {
    @POST("api/v1/diary")
    suspend fun writeDiary(
        @Body requestWriteDiaryDto: RequestWriteDiaryDto
    ): ApiResponse<ResponseWriteDiaryDto>
}
