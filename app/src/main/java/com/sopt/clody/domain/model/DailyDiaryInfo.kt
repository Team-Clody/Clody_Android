package com.sopt.clody.domain.model

/**
 * 홈 화면에서 월별 달력 하단에 일별 일기 정보를 위한 데이터 클래스
 *
 * @property diaryList 해당 일에 작성한 일기의 내용
 * @property isDraft 해당 일에 임시 저장 일기의 존재 여부
 *
 */
data class DailyDiaryInfo(
    val diaryList: List<String> = listOf(),
    val isDraft: Boolean = false,
)
