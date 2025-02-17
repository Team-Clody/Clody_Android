package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.TokenReissueService
import com.sopt.clody.data.remote.datasource.TokenReissueDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.response.TokenReissueResponseDto
import javax.inject.Inject

class TokenReissueDataSourceImpl @Inject constructor(
    private val tokenReissueService: TokenReissueService
) : TokenReissueDataSource {
    override suspend fun getReissueToken(authorization: String): ApiResponse<TokenReissueResponseDto> =
        tokenReissueService.reissue(authorization)
}

