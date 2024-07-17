package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.AuthDataSource
import com.sopt.clody.data.remote.dto.request.LoginRequestDto
import com.sopt.clody.data.remote.dto.response.LoginResponseDto
import com.sopt.clody.data.remote.dto.request.SignUpRequestDto
import com.sopt.clody.data.remote.dto.response.SignUpResponseDto
import com.sopt.clody.data.repository.AuthRepository
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {

    override suspend fun signIn(authorization: String, requestSignInDto: LoginRequestDto): Result<LoginResponseDto> {
        return runCatching {
            val response = authDataSource.signIn(authorization, requestSignInDto).handleApiResponse().getOrThrow()
            response
        }
    }

    override suspend fun signUp(authorization: String, requestSignUpDto: SignUpRequestDto): Result<SignUpResponseDto> {
        return runCatching {
            val response = authDataSource.signUp(authorization, requestSignUpDto)
            response.handleApiResponse().getOrThrow()
        }
    }
}

