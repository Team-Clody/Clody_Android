package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.TokenReissueResponseDto

interface TokenReissueDataSource {
    suspend fun getReissueToken(authorization: String): ApiResponse<TokenReissueResponseDto>
}
