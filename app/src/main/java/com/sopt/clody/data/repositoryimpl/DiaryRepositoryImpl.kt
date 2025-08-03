package com.sopt.clody.data.repositoryimpl

import com.sopt.clody.data.remote.datasource.DiaryRemoteDataSource
import com.sopt.clody.data.remote.dto.request.SaveDraftDiaryRequestDto
import com.sopt.clody.domain.repository.DiaryRepository
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource,
) : DiaryRepository {

    override suspend fun writeDiary(date: String, content: List<String>) =
        diaryRemoteDataSource.writeDiary(date, content)

    override suspend fun deleteDailyDiary(year: Int, month: Int, day: Int) =
        diaryRemoteDataSource.deleteDailyDiary(year, month, day)

    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int) =
        diaryRemoteDataSource.getDailyDiariesData(year, month, date)

    override suspend fun getDiaryTime(year: Int, month: Int, date: Int) =
        diaryRemoteDataSource.getDiaryTime(year, month, date)

    override suspend fun getMonthlyCalendarData(year: Int, month: Int) =
        diaryRemoteDataSource.getMonthlyCalendarData(year, month)

    override suspend fun getMonthlyDiary(year: Int, month: Int) =
        diaryRemoteDataSource.getMonthlyDiary(year, month)

    override suspend fun getReplyDiary(year: Int, month: Int, date: Int) =
        diaryRemoteDataSource.getReplyDiary(year, month, date)

    override suspend fun fetchDraftDiary(year: Int, month: Int, date: Int) =
        diaryRemoteDataSource.fetchDraftDiary(year, month, date).map { it.toDomain() }

    override suspend fun saveDraftDiary(date: String, contents: List<String>): Result<Unit> {
        val request = SaveDraftDiaryRequestDto(date, contents)
        return diaryRemoteDataSource.saveDraftDiary(request)
    }
}
