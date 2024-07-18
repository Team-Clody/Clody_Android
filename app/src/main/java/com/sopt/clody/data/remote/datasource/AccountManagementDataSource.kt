package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.ResponseUserInfoDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface AccountManagementDataSource {
    suspend fun getUserInfo(): ApiResponse<ResponseUserInfoDto>

    suspend fun revokeAccount(): ApiResponse<Unit>
}
