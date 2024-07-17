package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.LoginRequestDto
import com.sopt.clody.data.remote.dto.LoginResponseDto
import com.sopt.clody.data.remote.dto.SignUpRequestDto
import com.sopt.clody.data.remote.dto.SignUpResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/auth/signin")
    suspend fun postLogin(
        @Header("Authorization") accessToken: String,
        @Body body: LoginRequestDto
    ): ApiResponse<LoginResponseDto>

    @POST("api/v1/auth/signup")
    suspend fun signUp(
        @Header("Authorization") authorization: String,
        @Body signUpRequestDto: SignUpRequestDto
    ): ApiResponse<SignUpResponseDto>


}
