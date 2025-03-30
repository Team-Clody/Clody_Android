package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto

interface AccountManagementDataSource {
    suspend fun getUserInfo(): ApiResponse<UserInfoResponseDto>
    suspend fun modifyNickname(modifyNicknameRequestDto: ModifyNicknameRequestDto): ApiResponse<ModifyNicknameResponseDto>
    suspend fun revokeAccount(): ApiResponse<Unit>
}
