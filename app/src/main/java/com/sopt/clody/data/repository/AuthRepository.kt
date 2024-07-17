package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.LoginRequestDto
import com.sopt.clody.data.remote.dto.LoginResponseDto
import com.sopt.clody.data.remote.dto.SignUpRequestDto
import com.sopt.clody.data.remote.dto.SignUpResponseDto


interface AuthRepository {
    suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): Result<LoginResponseDto>
    suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): Result<SignUpResponseDto>
}
