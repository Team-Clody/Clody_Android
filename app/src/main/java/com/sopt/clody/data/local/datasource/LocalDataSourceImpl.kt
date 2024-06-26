package com.sopt.clody.data.local.datasource

import com.sopt.clody.data.local.entity.ExampleEntity
import com.sopt.clody.data.local.dao.ExampleDao
import javax.inject.Inject


// LocalDataSource의 구현체, ExampleDao를 통해 실제 데이터베이스 작업을 수행
class LocalDataSourceImpl @Inject constructor(
    private val exampleDao: ExampleDao
) : LocalDataSource {
    override suspend fun insertExample(example: ExampleEntity) {
        try {
            exampleDao.insert(example)
        } catch (e: Exception) {
            // 에러 처리 로직을 여기에 넣으면 됨
        }
    }

    override suspend fun getAllExamples(): List<ExampleEntity> {
        return try {
            exampleDao.getAllExamples()
        } catch (e: Exception) {
            // 에러 처리 로직을 여기에 넣으면 됨
           emptyList()
        }
    }
}
