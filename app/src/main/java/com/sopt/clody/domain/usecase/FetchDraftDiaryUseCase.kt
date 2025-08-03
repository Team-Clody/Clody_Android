package com.sopt.clody.domain.usecase

import com.sopt.clody.domain.repository.DiaryRepository
import javax.inject.Inject

class FetchDraftDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(year: Int, month: Int, day: Int) =
        diaryRepository.fetchDraftDiary(year, month, day)
}
