package com.sopt.clody.datasource

import com.sopt.clody.data.remote.datasource.DiaryRemoteDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.SaveDraftDiaryRequestDto
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.DraftDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DraftDiaryCreatedResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto

class FakeDiaryRemoteDataSource : DiaryRemoteDataSource {

    var draftDiariesResponse: ApiResponse<DraftDiariesResponseDto>? = null
    var saveDraftResponse: ApiResponse<DraftDiaryCreatedResponseDto>? = null

    override suspend fun writeDiary(date: String, content: List<String>): ApiResponse<WriteDiaryResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun deleteDailyDiary(year: Int, month: Int, date: Int): ApiResponse<DailyDiariesResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): ApiResponse<DailyDiariesResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getDiaryTime(year: Int, month: Int, date: Int): ApiResponse<DiaryTimeResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getMonthlyCalendarData(year: Int, month: Int): ApiResponse<MonthlyCalendarResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getMonthlyDiary(year: Int, month: Int): ApiResponse<MonthlyDiaryResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): ApiResponse<ReplyDiaryResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun fetchDraftDiary(
        year: Int,
        month: Int,
        date: Int,
    ): ApiResponse<DraftDiariesResponseDto> {
        return draftDiariesResponse
            ?: throw IllegalStateException("draftDiariesResponse not set")
    }

    override suspend fun saveDraftDiary(
        request: SaveDraftDiaryRequestDto,
    ): ApiResponse<DraftDiaryCreatedResponseDto> {
        return saveDraftResponse
            ?: throw IllegalStateException("saveDraftResponse not set")
    }

    fun setDraftDiariesResponse(list: List<String>) {
        draftDiariesResponse = ApiResponse(
            status = 200,
            message = "성공",
            data = DraftDiariesResponseDto(draftDiaries = list),
        )
    }

    fun setSaveDraftDiaryResponse(createdAt: String) {
        saveDraftResponse = ApiResponse(
            status = 201,
            message = "성공",
            data = DraftDiaryCreatedResponseDto(createdAt),
        )
    }
}
