package com.sopt.clody.domain.repository

import com.sopt.clody.data.remote.dto.request.GoogleSignUpRequestDto
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.dto.response.LoginResponseDto
import com.sopt.clody.data.remote.dto.response.SignUpResponseDto

interface AuthRepository {
    suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): Result<LoginResponseDto>
    suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): Result<SignUpResponseDto>
    suspend fun signUpWithGoogle(googleSignUpRequestDto: GoogleSignUpRequestDto): Result<SignUpResponseDto>
}
