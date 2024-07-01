package com.sopt.clody.domain.usecase

import com.sopt.clody.data.repository.ExampleRepository
import com.sopt.clody.domain.model.ExampleModel
import javax.inject.Inject

class GetAllExamplesUseCase @Inject constructor(
    private val exampleRepository: ExampleRepository
) {
    suspend operator fun invoke(): Result<List<ExampleModel>> {
        return exampleRepository.getAllExamples()
    }

}
