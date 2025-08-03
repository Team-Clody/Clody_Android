package com.sopt.clody.data.remote.datasourceimpl

import com.sopt.clody.data.remote.api.DiaryService
import com.sopt.clody.data.remote.datasource.DiaryRemoteDataSource
import com.sopt.clody.data.remote.dto.request.SaveDraftDiaryRequestDto
import com.sopt.clody.data.remote.dto.request.WriteDiaryRequestDto
import com.sopt.clody.data.remote.util.safeApiCall
import com.sopt.clody.presentation.utils.network.ErrorMessageProvider
import javax.inject.Inject

class DiaryRemoteDataSourceImpl @Inject constructor(
    private val diaryService: DiaryService,
    private val errorMessageProvider: ErrorMessageProvider,
) : DiaryRemoteDataSource {

    override suspend fun writeDiary(lang: String, date: String, content: List<String>) =
        safeApiCall(errorMessageProvider) { diaryService.writeDiary(lang, WriteDiaryRequestDto(date, content)) }

    override suspend fun deleteDailyDiary(year: Int, month: Int, date: Int) =
        safeApiCall(errorMessageProvider) { diaryService.deleteDailyDiary(year, month, date) }

    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int) =
        safeApiCall(errorMessageProvider) { diaryService.getDailyDiariesData(year, month, date) }

    override suspend fun getDiaryTime(year: Int, month: Int, date: Int) =
        safeApiCall(errorMessageProvider) { diaryService.getDiaryTime(year, month, date) }

    override suspend fun getMonthlyCalendarData(year: Int, month: Int) =
        safeApiCall(errorMessageProvider) { diaryService.getMonthlyCalendarData(year, month) }

    override suspend fun getMonthlyDiary(year: Int, month: Int) =
        safeApiCall(errorMessageProvider) { diaryService.getMonthlyDiary(year, month) }

    override suspend fun getReplyDiary(year: Int, month: Int, date: Int) =
        safeApiCall(errorMessageProvider) { diaryService.getReplyDiary(year, month, date) }

    override suspend fun fetchDraftDiary(year: Int, month: Int, date: Int) =
        safeApiCall(errorMessageProvider) { diaryService.fetchDraftDiary(year, month, date) }

    override suspend fun saveDraftDiary(request: SaveDraftDiaryRequestDto) =
        safeApiCall(errorMessageProvider) { diaryService.saveDraftDiary(request) }
}
