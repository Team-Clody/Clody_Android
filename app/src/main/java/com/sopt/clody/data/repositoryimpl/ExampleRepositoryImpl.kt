package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.RemoteDataSource
import com.sopt.clody.data.remote.dto.ExampleRequestDto
import com.sopt.clody.data.remote.mapper.toDomainModel
import com.sopt.clody.data.repository.ExampleRepository
import com.sopt.clody.domain.model.ExampleModel
import com.sopt.clody.presentation.utils.extension.handleApiResponse
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ExampleRepository {

    override suspend fun getAllExamples(): Result<List<ExampleModel>> {
        return runCatching {
            val remoteApiResponse = remoteDataSource.getExampleData()
            val exampleDtos = remoteApiResponse.handleApiResponse().getOrThrow()
            exampleDtos.map { it.toDomainModel() }
        }
    }

    override suspend fun postExample(exampleRequestDto: ExampleRequestDto): Result<Unit> {
        return runCatching {
            remoteDataSource.postExampleData(exampleRequestDto).handleApiResponse().getOrThrow()
        }
    }
}
