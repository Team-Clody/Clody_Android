package com.sopt.clody.data.remote.api

import com.sopt.clody.data.remote.dto.base.ApiResponse
import com.sopt.clody.data.remote.dto.request.WriteDiaryRequestDto
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.DiaryTimeResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.ReplyDiaryResponseDto
import com.sopt.clody.data.remote.dto.response.WriteDiaryResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiaryService {
    @POST("api/v1/diary")
    suspend fun writeDiary(
        @Body writeDiaryRequestDto: WriteDiaryRequestDto
    ): ApiResponse<WriteDiaryResponseDto>

    @DELETE("api/v1/diary")
    suspend fun deleteDailyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
    ): ApiResponse<DailyDiariesResponseDto>

    @GET("api/v1/diary")
    suspend fun getDailyDiariesData(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
    ): ApiResponse<DailyDiariesResponseDto>

    @GET("api/v1/diary/time")
    suspend fun getDiaryTime(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
    ): ApiResponse<DiaryTimeResponseDto>

    @GET("api/v1/calendar")
    suspend fun getMonthlyCalendarData(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): ApiResponse<MonthlyCalendarResponseDto>

    @GET("api/v1/calendar/list")
    suspend fun getMonthlyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
    ): ApiResponse<MonthlyDiaryResponseDto>

    @GET("api/v1/reply")
    suspend fun getReplyDiary(
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("date") date: Int
    ): ApiResponse<ReplyDiaryResponseDto>
}
