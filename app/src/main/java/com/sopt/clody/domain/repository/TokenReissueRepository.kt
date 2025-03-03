package com.sopt.clody.domain.repository

import com.sopt.clody.data.remote.dto.response.TokenReissueResponseDto

interface TokenReissueRepository {
    suspend fun getReissueToken(
        authorization: String,
    ): Result<TokenReissueResponseDto>
}
