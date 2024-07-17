package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.response.LoginResponseDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.dto.response.SignUpResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface AuthDataSource {
    suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): ApiResponse<LoginResponseDto>
    suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): ApiResponse<SignUpResponseDto>

}
