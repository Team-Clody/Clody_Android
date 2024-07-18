package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.RequestModifyNicknameDto
import com.sopt.clody.data.remote.dto.ResponseModifyNicknameDto
import com.sopt.clody.data.remote.dto.ResponseUserInfoDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface AccountManagementDataSource {
    suspend fun getUserInfo(): ApiResponse<ResponseUserInfoDto>

    suspend fun ModifyNickname(
        requestModifyNicknameDto: RequestModifyNicknameDto
    ) : ApiResponse<ResponseModifyNicknameDto>
  
    suspend fun revokeAccount(): ApiResponse<Unit>
}
