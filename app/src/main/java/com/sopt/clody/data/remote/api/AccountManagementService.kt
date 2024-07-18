package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.ResponseUserInfoDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.DELETE
import retrofit2.http.GET

interface AccountManagementService {
    @GET("api/v1/user/info")
    suspend fun getUserInfo(): ApiResponse<ResponseUserInfoDto>

    @DELETE("api/v1/user/revoke")
    suspend fun revokeAccount() : ApiResponse<Unit>
}
