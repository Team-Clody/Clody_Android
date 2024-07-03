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
    Font(R.font.pretendard_semibold, FontWeight.SemiBold, FontStyle.Normal)
)

private val pretendardTextStyle = TextStyle(
    fontFamily = pretendardFontFamily,
    fontWeight = FontWeight.SemiBold,
    letterSpacing = (-0.2).sp,
)

val defaultClodyTypography = ClodyTypography(
    head1 = pretendardTextStyle.copy(
        fontSize = 22.sp,
        lineHeight = 33.sp,
    ),
    head2 = pretendardTextStyle.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp,
    ),
    head3 = pretendardTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 27.sp,
    ),
    head4 = pretendardTextStyle.copy(
        fontSize = 17.sp,
        lineHeight = 25.5.sp,
    ),
    head5 = pretendardTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    body1SemiBold = pretendardTextStyle.copy(
        fontSize = 15.sp,
        lineHeight = 22.5.sp,
    ),
    body1Medium = pretendardTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.5.sp,
    ),
    body2SemiBold = pretendardTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    body2Medium = pretendardTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    body3SemiBold = pretendardTextStyle.copy(
        fontSize = 13.sp,
        lineHeight = 19.5.sp,
    ),
    body3Medium = pretendardTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 19.5.sp,
    ),
    detail1SemiBold = pretendardTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    detail1Medium = pretendardTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    detail2SemiBold = pretendardTextStyle.copy(
        fontSize = 10.sp,
        lineHeight = 15.sp,
    ),
    detail2Medium = pretendardTextStyle.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 15.sp,
    )
)

@Immutable
data class ClodyTypography(
    val head1: TextStyle,
    val head2: TextStyle,
    val head3: TextStyle,
    val head4: TextStyle,
    val head5: TextStyle,
    val body1SemiBold: TextStyle,
    val body1Medium: TextStyle,
    val body2SemiBold: TextStyle,
    val body2Medium: TextStyle,
    val body3SemiBold: TextStyle,
    val body3Medium: TextStyle,
    val detail1SemiBold: TextStyle,
    val detail1Medium: TextStyle,
    val detail2SemiBold: TextStyle,
    val detail2Medium: TextStyle
)

val LocalTypography = staticCompositionLocalOf {
    defaultClodyTypography
}
