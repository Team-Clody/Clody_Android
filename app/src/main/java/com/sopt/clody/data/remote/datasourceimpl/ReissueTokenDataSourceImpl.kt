package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.ReIssueService
import com.sopt.clody.data.remote.datasource.ReissueTokenDataSource
import com.sopt.clody.data.remote.dto.ReissueTokenResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class ReissueTokenDataSourceImpl @Inject constructor(
    private val reIssueService: ReIssueService
) : ReissueTokenDataSource {
    override suspend fun getReissueToken(authorization: String): ApiResponse<ReissueTokenResponseDto> {
        return reIssueService.reissue(authorization)
    }
}

