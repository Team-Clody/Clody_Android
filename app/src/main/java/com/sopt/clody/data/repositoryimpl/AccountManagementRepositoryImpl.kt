package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto
import com.sopt.clody.data.remote.util.handleApiResponse
import com.sopt.clody.domain.repository.AccountManagementRepository
import javax.inject.Inject

class AccountManagementRepositoryImpl @Inject constructor(
    private val accountManagementDataSource: AccountManagementDataSource
) : AccountManagementRepository {
    override suspend fun getUserInfo(): Result<UserInfoResponseDto> =
        runCatching {
            accountManagementDataSource.getUserInfo().handleApiResponse().getOrThrow()
        }

    override suspend fun modifyNickname(modifyNicknameRequestDto: ModifyNicknameRequestDto): Result<ModifyNicknameResponseDto> =
        runCatching {
            accountManagementDataSource.modifyNickname(modifyNicknameRequestDto).handleApiResponse().getOrThrow()
        }

    override suspend fun revokeAccount(): Result<Unit> =
        runCatching {
            accountManagementDataSource.revokeAccount().handleApiResponse().getOrThrow()
        }
}
