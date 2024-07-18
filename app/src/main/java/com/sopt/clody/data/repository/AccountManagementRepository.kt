package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.RequestModifyNicknameDto
import com.sopt.clody.data.remote.dto.ResponseModifyNicknameDto
import com.sopt.clody.data.remote.dto.ResponseUserInfoDto

interface AccountManagementRepository {
    suspend fun getUserInfo(): Result<ResponseUserInfoDto>
  
    suspend fun modifyNickname(
        requestModifyNicknameDto: RequestModifyNicknameDto
    ): Result<ResponseModifyNicknameDto>

    suspend fun revokeAccount(): Result<Unit>
    
}
