package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.GoogleSignUpRequestDto
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.dto.response.LoginResponseDto
import com.sopt.clody.data.remote.dto.response.SignUpResponseDto

interface AuthDataSource {
    suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): ApiResponse<LoginResponseDto>
    suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): ApiResponse<SignUpResponseDto>
    suspend fun signUpWithGoogle(googleSignUpRequestDto: GoogleSignUpRequestDto): ApiResponse<SignUpResponseDto>
}
