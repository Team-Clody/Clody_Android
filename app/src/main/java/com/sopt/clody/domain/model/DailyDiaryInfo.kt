package com.sopt.clody.domain.model

data class DailyDiaryInfo(
    val diaryList: List<String> = listOf(),
    val isDraft: Boolean = false,
)
