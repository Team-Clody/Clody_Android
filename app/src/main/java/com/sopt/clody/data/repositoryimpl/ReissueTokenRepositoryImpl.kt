package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.ReissueTokenDataSource
import com.sopt.clody.data.remote.dto.ReissueTokenResponseDto
import com.sopt.clody.data.repository.ReissueTokenRepository
import javax.inject.Inject

class ReissueTokenRepositoryImpl @Inject constructor(
    private val reissueTokenDataSource: ReissueTokenDataSource
) : ReissueTokenRepository {
    override suspend fun getReissueToken(
        authorization: String,
    ): Result<ReissueTokenResponseDto> =
        runCatching {
            val authorizationHeader = "Bearer $authorization"
            reissueTokenDataSource.getReissueToken(authorizationHeader).data
        }
}
