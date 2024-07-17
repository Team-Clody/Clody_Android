package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.ReissueTokenResponseDto

interface ReissueTokenRepository {
    suspend fun getReissueToken(
        authorization: String,
    ): Result<ReissueTokenResponseDto>
}
