package com.sopt.clody.domain.model

import java.time.LocalDate


data class DiaryDateData(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue
)
