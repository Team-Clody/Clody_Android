package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.local.mapper.ExampleEntityMapper
import com.sopt.clody.data.local.datasource.LocalDataSource
import com.sopt.clody.data.remote.mapper.ExampleMapper
import com.sopt.clody.data.remote.datasource.RemoteDataSource
import com.sopt.clody.domain.model.ExampleModel
import com.sopt.clody.domain.repository.ExampleRepository
import com.sopt.clody.utils.extension.handleApiResponse
import javax.inject.Inject

class ExampleRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ExampleRepository {

    override suspend fun getAllExamples(): List<ExampleModel> {
        val remoteApiResponse = remoteDataSource.getExampleData()
        val remoteData = remoteApiResponse.handleApiResponse().getOrThrow()

        val localData = localDataSource.getAllExamples()

        val combinedData = remoteData.map { ExampleMapper.toDomainModel(it) } + localData.map { ExampleEntityMapper.toDomainModel(it) }
        return combinedData
    }

    override suspend fun insertExample(example: ExampleModel) {
        // 로컬 데이터 소스에 데이터 삽입
        localDataSource.insertExample(ExampleEntityMapper.toEntity(example))
    }
}
