package com.sopt.clody.datasource

import com.sopt.clody.data.remote.datasource.DiaryRemoteDataSource
import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.SaveDraftDiaryRequestDto
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.DraftDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto
import com.sopt.clody.data.remote.util.ApiError

class FakeDiaryRemoteDataSource : DiaryRemoteDataSource {

    var draftDiariesResponse: ApiResponse<DraftDiariesResponseDto>? = null
    var saveDraftResponse: ApiResponse<Unit>? = null

    override suspend fun writeDiary(date: String, content: List<String>): Result<WriteDiaryResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun deleteDailyDiary(year: Int, month: Int, date: Int): Result<DailyDiariesResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getDailyDiariesData(year: Int, month: Int, date: Int): Result<DailyDiariesResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getDiaryTime(year: Int, month: Int, date: Int): Result<DiaryTimeResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getMonthlyCalendarData(year: Int, month: Int): Result<MonthlyCalendarResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getMonthlyDiary(year: Int, month: Int): Result<MonthlyDiaryResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun getReplyDiary(year: Int, month: Int, date: Int): Result<ReplyDiaryResponseDto> {
        throw NotImplementedError()
    }

    override suspend fun fetchDraftDiary(
        year: Int,
        month: Int,
        date: Int,
    ): Result<DraftDiariesResponseDto> {
        return draftDiariesResponse?.let { Result.success(it.data!!) }
            ?: Result.failure(ApiError("draftDiariesResponse not set"))
    }

    override suspend fun saveDraftDiary(
        request: SaveDraftDiaryRequestDto,
    ): Result<Unit> {
        return saveDraftResponse?.let { Result.success(Unit) }
            ?: Result.failure(ApiError("saveDraftResponse not set"))
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
            data = Unit,
        )
    }
}
