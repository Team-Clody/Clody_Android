package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface AccountManagementDataSource {
    suspend fun getUserInfo(): ApiResponse<UserInfoResponseDto>

    suspend fun ModifyNickname(
        modifyNicknameRequestDto: ModifyNicknameRequestDto
    ) : ApiResponse<ModifyNicknameResponseDto>

    suspend fun revokeAccount(): ApiResponse<Unit>
}
