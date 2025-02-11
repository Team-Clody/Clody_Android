package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto
import com.sopt.clody.data.repository.AccountManagementRepository
import com.sopt.clody.data.remote.util.handleApiResponse
import javax.inject.Inject

class AccountManagementRepositoryImpl @Inject constructor(
    private val accountManagementDataSource: AccountManagementDataSource
) : AccountManagementRepository {
    override suspend fun getUserInfo(): Result<UserInfoResponseDto> {
        return runCatching {
            accountManagementDataSource.getUserInfo().handleApiResponse().getOrThrow()
        }
    }

    override suspend fun modifyNickname(
        modifyNicknameRequestDto: ModifyNicknameRequestDto
    ): Result<ModifyNicknameResponseDto> {
        return runCatching {
            accountManagementDataSource.modifyNickname(modifyNicknameRequestDto).handleApiResponse().getOrThrow()
        }
    }

    override suspend fun revokeAccount(): Result<Unit> {
        return runCatching {
            accountManagementDataSource.revokeAccount().handleApiResponse().getOrThrow()
        }
    }
}
