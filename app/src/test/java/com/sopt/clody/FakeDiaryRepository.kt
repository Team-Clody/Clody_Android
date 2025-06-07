package com.sopt.clody

import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto
import com.sopt.clody.domain.model.CreatedDraftDiaryInfo
import com.sopt.clody.domain.model.DraftDiaryContents
import com.sopt.clody.domain.repository.DiaryRepository

class FakeDiaryRepository : DiaryRepository {
    var draftDiaryResult: Result<DraftDiaryContents>? = null
    var saveDraftResult: Result<CreatedDraftDiaryInfo>? = null
    private var draftDiaryContents: DraftDiaryContents? = null

    override suspend fun writeDiary(date: String, content: List<String>): Result<WriteDiaryResponseDto> {
        throw NotImplementedError("This method is not implemented in FakeDiaryRepository")
    }

    override suspend fun deleteDailyDiary(year: Int, month: Int, day: Int): Result<DailyDiariesResponseDto> {
        throw NotImplementedError("This method is not implemented in FakeDiaryRepository")
    }

    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): Result<DailyDiariesResponseDto> {
        throw NotImplementedError("This method is not implemented in FakeDiaryRepository")
    }

    override suspend fun getDiaryTime(year: Int, month: Int, date: Int): Result<DiaryTimeResponseDto> {
        throw NotImplementedError("This method is not implemented in FakeDiaryRepository")
    }

    override suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto> {
        throw NotImplementedError("This method is not implemented in FakeDiaryRepository")
    }

    override suspend fun getMonthlyDiary(year: Int, month: Int): Result<MonthlyDiaryResponseDto> {
        throw NotImplementedError("This method is not implemented in FakeDiaryRepository")
    }

    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): Result<ReplyDiaryResponseDto> {
        throw NotImplementedError("This method is not implemented in FakeDiaryRepository")
    }

    override suspend fun fetchDraftDiary(year: Int, month: Int, date: Int): Result<DraftDiaryContents> {
        return draftDiaryContents?.let { Result.success(it) }
            ?: Result.failure(IllegalStateException("No draft found"))
    }

    override suspend fun saveDraftDiary(contents: List<String>): Result<CreatedDraftDiaryInfo> {
        draftDiaryContents = DraftDiaryContents(contents)
        return Result.success(CreatedDraftDiaryInfo(createdAt = "2024-06-01T00:00:00"))
    }
}
