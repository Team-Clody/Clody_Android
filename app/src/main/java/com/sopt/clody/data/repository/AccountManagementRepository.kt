package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.ResponseUserInfoDto

interface AccountManagementRepository {
    suspend fun getUserInfo(): Result<ResponseUserInfoDto>

    suspend fun revoke(): Result<Unit>
}
