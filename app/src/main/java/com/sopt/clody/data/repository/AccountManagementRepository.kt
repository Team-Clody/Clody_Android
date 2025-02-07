package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto

interface AccountManagementRepository {
    suspend fun getUserInfo(): Result<UserInfoResponseDto>

    suspend fun modifyNickname(
        modifyNicknameRequestDto: ModifyNicknameRequestDto
    ): Result<ModifyNicknameResponseDto>

    suspend fun revokeAccount(): Result<Unit>

}
