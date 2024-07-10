package com.sopt.clody.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sopt.clody.R

val pretendardFontFamily = FontFamily(
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal)
)

private val pretendardTextStyle = TextStyle(
    fontFamily = pretendardFontFamily,
    letterSpacing = (-0.2).sp,
)

val defaultClodyTypography = ClodyTypography(
    head1 = pretendardTextStyle.copy(
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 33.sp,
    ),
    head2 = pretendardTextStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 30.sp,
    ),
    head3 = pretendardTextStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 27.sp,
    ),
    head3Medium = pretendardTextStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 27.sp,
    ),
    head4 = pretendardTextStyle.copy(
        fontSize = 17.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 25.5.sp,
    ),
    body1SemiBold = pretendardTextStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 24.sp,
    ),
    body1Medium = pretendardTextStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp,
    ),
    body2SemiBold = pretendardTextStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 22.5.sp,
    ),
    body2Medium = pretendardTextStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.5.sp,
    ),
    body3SemiBold = pretendardTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 21.sp,
    ),
    body3Medium = pretendardTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 21.sp,
    ),
    body3Regular = pretendardTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp,
    ),
    body4Medium = pretendardTextStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 19.5.sp
    ),
    body4SemiBold = pretendardTextStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 19.5.sp
    ),
    detail1SemiBold = pretendardTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 18.sp,
    ),
    detail1Medium = pretendardTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
    ),
    detail1Regular = pretendardTextStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp
    ),
    detail2SemiBold = pretendardTextStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 15.sp,
    ),
    detail2Medium = pretendardTextStyle.copy(
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 15.sp,
    ),
    letterMedium = pretendardTextStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 26.6.sp
    ),
)

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

val LocalTypography = staticCompositionLocalOf {
    defaultClodyTypography
}
