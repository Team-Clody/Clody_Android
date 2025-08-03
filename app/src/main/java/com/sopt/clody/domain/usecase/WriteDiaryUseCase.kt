package com.sopt.clody.domain.usecase

import com.sopt.clody.domain.repository.DiaryRepository
import javax.inject.Inject

class WriteDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(date: String, content: List<String>) =
        diaryRepository.writeDiary(date, content)
}
