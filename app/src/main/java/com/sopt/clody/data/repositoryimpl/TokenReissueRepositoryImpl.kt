package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.TokenReissueDataSource
import com.sopt.clody.data.remote.dto.response.TokenReissueResponseDto
import com.sopt.clody.domain.repository.TokenReissueRepository
import javax.inject.Inject

class TokenReissueRepositoryImpl @Inject constructor(
    private val tokenReissueDataSource: TokenReissueDataSource
) : TokenReissueRepository {
    override suspend fun getReissueToken(authorization: String, ): Result<TokenReissueResponseDto> =
        runCatching {
            val authorizationHeader = "Bearer $authorization"
            tokenReissueDataSource.getReissueToken(authorizationHeader).data
        }
}
