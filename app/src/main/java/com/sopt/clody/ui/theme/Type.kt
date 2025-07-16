package com.sopt.clody.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.sopt.clody.R

@Immutable
data class ClodyTypography(
    val head1: TextStyle,
    val head2: TextStyle,
    val head3: TextStyle,
    val head3Medium: TextStyle,
    val head4: TextStyle,
    val body1SemiBold: TextStyle,
    val body1Medium: TextStyle,
    val body2SemiBold: TextStyle,
    val body2Medium: TextStyle,
    val body3SemiBold: TextStyle,
    val body3Medium: TextStyle,
    val body3Regular: TextStyle,
    val body4SemiBold: TextStyle,
    val body4Medium: TextStyle,
    val detail1SemiBold: TextStyle,
    val detail1Medium: TextStyle,
    val detail1Regular: TextStyle,
    val detail2SemiBold: TextStyle,
    val detail2Medium: TextStyle,
    val letterMedium: TextStyle,
)

fun TextUnit.lineHeight(ratio: Float): TextUnit = (this.value * ratio).sp

val pretendardFontFamily = FontFamily(
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal),
)

private val pretendardKoreanTextStyle = TextStyle(
    fontFamily = pretendardFontFamily,
    letterSpacing = (-0.2).sp,
)

private val pretendardEnglishTextStyle = TextStyle(
    fontFamily = pretendardFontFamily,
)

val clodyKoreanTypography = ClodyTypography(
    head1 = pretendardKoreanTextStyle.copy(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 22.sp.lineHeight(1.5f),
    ),
    head2 = pretendardKoreanTextStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp.lineHeight(1.5f),
    ),
    head3 = pretendardKoreanTextStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 18.sp.lineHeight(1.5f),
    ),
    head3Medium = pretendardKoreanTextStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp.lineHeight(1.5f),
    ),
    head4 = pretendardKoreanTextStyle.copy(
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 17.sp.lineHeight(1.5f),
    ),
    body1SemiBold = pretendardKoreanTextStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp.lineHeight(1.5f),
    ),
    body1Medium = pretendardKoreanTextStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp.lineHeight(1.5f),
    ),
    body2SemiBold = pretendardKoreanTextStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 15.sp.lineHeight(1.5f),
    ),
    body2Medium = pretendardKoreanTextStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 15.sp.lineHeight(1.5f),
    ),
    body3SemiBold = pretendardKoreanTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 14.sp.lineHeight(1.5f),
    ),
    body3Medium = pretendardKoreanTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp.lineHeight(1.5f),
    ),
    body3Regular = pretendardKoreanTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 14.sp.lineHeight(1.5f),
    ),
    body4SemiBold = pretendardKoreanTextStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 13.sp.lineHeight(1.5f),
    ),
    body4Medium = pretendardKoreanTextStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 13.sp.lineHeight(1.5f),
    ),
    detail1SemiBold = pretendardKoreanTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 12.sp.lineHeight(1.5f),
    ),
    detail1Medium = pretendardKoreanTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 12.sp.lineHeight(1.5f),
    ),
    detail1Regular = pretendardKoreanTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 12.sp.lineHeight(1.5f),
    ),
    detail2SemiBold = pretendardKoreanTextStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 10.sp.lineHeight(1.5f),
    ),
    detail2Medium = pretendardKoreanTextStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 10.sp.lineHeight(1.5f),
    ),
    letterMedium = pretendardKoreanTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp.lineHeight(1.9f),
    ),
)

val clodyEnglishTypography = ClodyTypography(
    head1 = pretendardEnglishTextStyle.copy(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 22.sp.lineHeight(1.4f),
    ),
    head2 = pretendardEnglishTextStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.sp.lineHeight(1.4f),
    ),
    head3 = pretendardEnglishTextStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 18.sp.lineHeight(1.4f),
    ),
    head3Medium = pretendardEnglishTextStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp.lineHeight(1.4f),
    ),
    head4 = pretendardEnglishTextStyle.copy(
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 17.sp.lineHeight(1.4f),
    ),
    body1SemiBold = pretendardEnglishTextStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 16.sp.lineHeight(1.3f),
    ),
    body1Medium = pretendardEnglishTextStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp.lineHeight(1.3f),
    ),
    body2SemiBold = pretendardEnglishTextStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 15.sp.lineHeight(1.3f),
    ),
    body2Medium = pretendardEnglishTextStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 15.sp.lineHeight(1.3f),
    ),
    body3SemiBold = pretendardEnglishTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 14.sp.lineHeight(1.3f),
    ),
    body3Medium = pretendardEnglishTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp.lineHeight(1.3f),
    ),
    body3Regular = pretendardEnglishTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 14.sp.lineHeight(1.3f),
    ),
    body4SemiBold = pretendardEnglishTextStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 13.sp.lineHeight(1.3f),
    ),
    body4Medium = pretendardEnglishTextStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 13.sp.lineHeight(1.3f),
    ),
    detail1SemiBold = pretendardEnglishTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 12.sp.lineHeight(1.3f),
    ),
    detail1Medium = pretendardEnglishTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 12.sp.lineHeight(1.3f),
    ),
    detail1Regular = pretendardEnglishTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 12.sp.lineHeight(1.3f),
    ),
    detail2SemiBold = pretendardEnglishTextStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 10.sp.lineHeight(1.3f),
    ),
    detail2Medium = pretendardEnglishTextStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 10.sp.lineHeight(1.3f),
    ),
    letterMedium = pretendardEnglishTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 14.sp.lineHeight(1.8f),
    ),
)

val LocalClodyTypography = staticCompositionLocalOf { clodyEnglishTypography }
