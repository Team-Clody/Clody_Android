package com.sopt.clody.domain.usecase

import com.sopt.clody.domain.model.CreatedDraftDiaryInfo
import com.sopt.clody.domain.repository.DiaryRepository
import javax.inject.Inject

class SaveDraftDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(date: String, contents: List<String>): Result<CreatedDraftDiaryInfo> {
        return diaryRepository.saveDraftDiary(date, contents)
    }
}
