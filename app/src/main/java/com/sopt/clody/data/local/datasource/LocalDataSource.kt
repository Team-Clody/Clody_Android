package com.sopt.clody.data.local.datasource

import com.sopt.clody.data.local.entity.ExampleEntity

//로컬 데이터베이스와 상호작용하는 인터페이스
interface LocalDataSource {
    suspend fun insertExample(example: ExampleEntity)
    suspend fun getAllExamples(): List<ExampleEntity>
}
