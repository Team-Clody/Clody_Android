package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.AuthDataSource
import com.sopt.clody.data.remote.dto.LoginRequestDto
import com.sopt.clody.data.remote.dto.LoginResponseDto
import com.sopt.clody.data.remote.dto.SignUpRequestDto
import com.sopt.clody.data.remote.dto.SignUpResponseDto
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

