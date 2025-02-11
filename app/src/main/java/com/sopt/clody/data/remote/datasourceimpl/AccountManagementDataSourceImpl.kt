package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.AccountManagementService
import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class AccountManagementDataSourceImpl @Inject constructor(
    private val accountManagementService: AccountManagementService,
) : AccountManagementDataSource {
    override suspend fun getUserInfo(): ApiResponse<UserInfoResponseDto> =
        accountManagementService.getUserInfo()

    override suspend fun modifyNickname(modifyNicknameRequestDto: ModifyNicknameRequestDto): ApiResponse<ModifyNicknameResponseDto> =
        accountManagementService.modifyNickname(modifyNicknameRequestDto)

    override suspend fun revokeAccount(): ApiResponse<Unit> =
        accountManagementService.revokeAccount()
}
