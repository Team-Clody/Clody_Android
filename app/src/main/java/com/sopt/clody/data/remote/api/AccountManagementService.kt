package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.request.ModifyNicknameRequestDto
import com.sopt.clody.data.remote.dto.response.ModifyNicknameResponseDto
import com.sopt.clody.data.remote.dto.response.UserInfoResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface AccountManagementService {
    @GET("api/v1/user/info")
    suspend fun getUserInfo(): ApiResponse<UserInfoResponseDto>

    @PATCH("api/v1/user/nickname")
    suspend fun modifyNickname(
        @Body body: ModifyNicknameRequestDto
    ) : ApiResponse<ModifyNicknameResponseDto>

    @DELETE("api/v1/user/revoke")
    suspend fun revokeAccount() : ApiResponse<Unit>
}
