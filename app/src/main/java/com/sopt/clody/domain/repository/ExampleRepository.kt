package com.sopt.clody.domain.repository

import com.sopt.clody.domain.model.ExampleModel

// 도메인 레이어에서 사용할 리포지토리 인터페이스 정의
interface ExampleRepository {
    suspend fun getAllExamples(): List<ExampleModel>
    suspend fun insertExample(example: ExampleModel)
}
