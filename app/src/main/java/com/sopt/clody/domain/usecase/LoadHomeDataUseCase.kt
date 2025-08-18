package com.sopt.clody.domain.usecase

import com.sopt.clody.domain.model.MonthlyCalendarInfo
import com.sopt.clody.domain.repository.DiaryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class LoadHomeDataUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository,
) {
    suspend operator fun invoke(year: Int, month: Int, day: Int): Result<MonthlyCalendarInfo> =
        coroutineScope {
            try {
                val monthlyDiaryInfoDeferred =
                    async { diaryRepository.getMonthlyCalendarData(year, month) }
                val dailyDiaryInfoDeferred =
                    async { diaryRepository.getDailyDiariesData(year, month, day) }

                val monthlyDiaryInfo = monthlyDiaryInfoDeferred.await()
                val dailyDiaryInfo = dailyDiaryInfoDeferred.await()

                if (monthlyDiaryInfo.isSuccess && dailyDiaryInfo.isSuccess) {
                    Result.success(
                        MonthlyCalendarInfo(
                            year = year,
                            month = month,
                            totalCloverCount = monthlyDiaryInfo.getOrThrow().totalCloverCount,
                            dailyDiaryInfoList = monthlyDiaryInfo.getOrThrow().diaries.map { it ->
                                MonthlyCalendarInfo.DailyDiaryInfo(
                                    diaryCount = it.diaryCount,
                                    replyStatus = it.replyStatus,
                                    date = it.date,
                                    diaryList = dailyDiaryInfo.getOrThrow().diaries.map { it.content },
                                    isDeleted = dailyDiaryInfo.getOrThrow().isDeleted,
                                    isDraft = dailyDiaryInfo.getOrThrow().isDraft
                                )
                            }
                        )
                    )
                } else {
                    Result.failure(
                        Exception(
                            "Failed to load monthly diary info or daily diary info." +
                                    "monthlyDiaryInfo: ${monthlyDiaryInfo.exceptionOrNull()?.message}, " +
                                    "dailyDiaryInfo: ${dailyDiaryInfo.exceptionOrNull()?.message}"
                        )
                    )
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}

