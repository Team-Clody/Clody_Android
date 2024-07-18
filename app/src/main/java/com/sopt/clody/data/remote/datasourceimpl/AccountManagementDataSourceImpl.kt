package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.AccountManagementService
import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.dto.ResponseUserInfoDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class AccountManagementDataSourceImpl @Inject constructor(
    private val accountManagementSevice: AccountManagementService
) : AccountManagementDataSource {
    override suspend fun getUserInfo(): ApiResponse<ResponseUserInfoDto> {
        return accountManagementSevice.getUserInfo()
    }

    override suspend fun revokeAccount(): ApiResponse<Unit> {
        return accountManagementSevice.revokeAccount()
    }
}
