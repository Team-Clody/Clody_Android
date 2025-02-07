package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ResponseModifyNicknameDto
import com.sopt.clody.data.remote.dto.response.ResponseUserInfoDto

interface AccountManagementRepository {
    suspend fun getUserInfo(): Result<ResponseUserInfoDto>

    suspend fun modifyNickname(
        modifyNicknameRequestDto: ModifyNicknameRequestDto
    ): Result<ResponseModifyNicknameDto>

    suspend fun revokeAccount(): Result<Unit>

}
