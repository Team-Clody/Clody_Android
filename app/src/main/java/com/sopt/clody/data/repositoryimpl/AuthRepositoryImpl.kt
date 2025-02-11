package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.AuthDataSource
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.dto.response.LoginResponseDto
import com.sopt.clody.data.remote.dto.response.SignUpResponseDto
import com.sopt.clody.data.remote.util.handleApiResponse
import com.sopt.clody.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): Result<LoginResponseDto> =
        runCatching {
            authDataSource.signIn(authorization, requestSignInDto).handleApiResponse().getOrThrow()
        }

    override suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): Result<SignUpResponseDto> =
        runCatching {
            authDataSource.signUp(authorization, requestSignUpDto).handleApiResponse().getOrThrow()
        }
}

