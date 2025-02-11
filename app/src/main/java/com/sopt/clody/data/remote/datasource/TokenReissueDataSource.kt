package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.response.TokenReissueResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface TokenReissueDataSource {
    suspend fun getReissueToken(
        authorization: String
    ): ApiResponse<TokenReissueResponseDto>
}
