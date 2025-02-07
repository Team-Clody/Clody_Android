package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ResponseModifyNicknameDto
import com.sopt.clody.data.remote.dto.response.ResponseUserInfoDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface AccountManagementDataSource {
    suspend fun getUserInfo(): ApiResponse<ResponseUserInfoDto>

    suspend fun ModifyNickname(
        modifyNicknameRequestDto: ModifyNicknameRequestDto
    ) : ApiResponse<ResponseModifyNicknameDto>

    suspend fun revokeAccount(): ApiResponse<Unit>
}
