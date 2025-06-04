package com.sopt.clody.domain.usecase

import com.sopt.clody.domain.model.DraftDiaryContents
import com.sopt.clody.domain.repository.DiaryRepository
import javax.inject.Inject

class FetchDraftDiaryUseCase @Inject constructor(
    private val repository: DiaryRepository,
) {
    suspend operator fun invoke(year: Int, month: Int, day: Int): Result<DraftDiaryContents> {
        return repository.fetchDraftDiary(year, month, day)
    }
}
