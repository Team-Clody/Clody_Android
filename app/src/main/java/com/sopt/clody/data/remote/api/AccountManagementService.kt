package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.RequestModifyNicknameDto
import com.sopt.clody.data.remote.dto.ResponseModifyNicknameDto
import com.sopt.clody.data.remote.dto.ResponseUserInfoDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface AccountManagementService {
    @GET("api/v1/user/info")
    suspend fun getUserInfo(): ApiResponse<ResponseUserInfoDto>

    @PATCH("api/v1/user/nickname")
    suspend fun modifyNickname(
        @Body body: RequestModifyNicknameDto
    ) : ApiResponse<ResponseModifyNicknameDto>
  
    @DELETE("api/v1/user/revoke")
    suspend fun revokeAccount() : ApiResponse<Unit>
}
