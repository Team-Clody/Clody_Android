package com.sopt.clody.data.remote.datasource

import com.sopt.clody.data.remote.dto.LoginRequestDto
import com.sopt.clody.data.remote.dto.LoginResponseDto
import com.sopt.clody.data.remote.dto.SignUpRequestDto
import com.sopt.clody.data.remote.dto.SignUpResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse

interface AuthDataSource {
    suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): ApiResponse<LoginResponseDto>
    suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): ApiResponse<SignUpResponseDto>

}
