package com.sopt.clody.presentation.ui.home.component

import java.time.LocalDate

data class DiaryDateData(
    val year: Int = LocalDate.now().year,
    val month: Int = LocalDate.now().monthValue,
)
