package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.AccountManagementDataSource
import com.sopt.clody.data.remote.dto.ResponseUserInfoDto
import com.sopt.clody.data.repository.AccountManagementRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class AccountManagementRepositoryImpl @Inject constructor(
    private val accountManagementDataSource: AccountManagementDataSource
) : AccountManagementRepository {
    override suspend fun getUserInfo(): Result<ResponseUserInfoDto> {
        return runCatching {
            accountManagementDataSource.getUserInfo().handleApiResponse().getOrThrow()
        }
    }

    override suspend fun revoke(): Result<Unit> {
        return runCatching {
            accountManagementDataSource.revoke().handleApiResponse().getOrThrow()
        }
    }
}
