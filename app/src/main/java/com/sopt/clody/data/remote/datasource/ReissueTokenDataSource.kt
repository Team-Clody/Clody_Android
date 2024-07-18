package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.response.ReissueTokenResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface ReissueTokenDataSource {
    suspend fun getReissueToken(
        authorization: String
    ): ApiResponse<ReissueTokenResponseDto>
}
