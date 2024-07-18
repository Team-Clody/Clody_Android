package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.AuthService
import com.sopt.clody.data.remote.datasource.AuthDataSource
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.response.LoginResponseDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.dto.response.SignUpResponseDto
import com.sopt.clody.data.remote.dto.base.ApiResponse
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authService: AuthService
) : AuthDataSource {

    override suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): ApiResponse<LoginResponseDto> {
        return authService.postLogin(authorization, requestSignInDto)
    }

    override suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): ApiResponse<SignUpResponseDto> {
        return authService.signUp(authorization, requestSignUpDto)
    }

}
