package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.AccountManagementService
import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class AccountManagementDataSourceImpl @Inject constructor(
    private val accountManagementSevice: AccountManagementService,
) : AccountManagementDataSource {
    override suspend fun getUserInfo(): ApiResponse<UserInfoResponseDto> {
        return accountManagementSevice.getUserInfo()
    }

    override suspend fun ModifyNickname(modifyNicknameRequestDto: ModifyNicknameRequestDto): ApiResponse<ModifyNicknameResponseDto> {
        return accountManagementSevice.modifyNickname(modifyNicknameRequestDto)
    }

    override suspend fun revokeAccount(): ApiResponse<Unit> {
        return accountManagementSevice.revokeAccount()
    }
}
