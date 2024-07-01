package com.sopt.clody.data.repository

import com.sopt.clody.data.remote.dto.ExampleRequestDto
import com.sopt.clody.domain.model.ExampleModel

interface ExampleRepository {
    suspend fun getAllExamples(): Result<List<ExampleModel>>
    suspend fun postExample(exampleRequestDto: ExampleRequestDto): Result<Unit>

}
