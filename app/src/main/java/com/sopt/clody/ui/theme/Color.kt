package com.sopt.clody.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val KakaoYellow = Color(0xFFFAE100)
val AppleBlack = Color(0xFF111111)

val Red = Color(0xFFFF5247)

// Green Scale
val LightGreenBack = Color(0xFFE5F9ED)
val LightGreen = Color(0xFFA9F1B0)
val MainGreen = Color(0xFF47D27D)
val DarkGreen = Color(0xFF1C9D5F)

// Yellow Scale
val LightYellow = Color(0xFFFFF5D0)
val MainYellow = Color(0xFFFFD84D)
val DarkYellow = Color(0xFFFFC700)

// Blue Scale
val LightBlue = Color(0xFFE8F3FF)
val Blue = Color(0xFF5277FC)

// Gray Scale
val Gray01 = Color(0xFF282A31)
val Gray02 = Color(0xFF3C3E48)
val Gray03 = Color(0xFF4A4C54)
val Gray04 = Color(0xFF9396A2)
val Gray05 = Color(0xFFA7A9B2)
val Gray06 = Color(0xFFC3C6D0)
val Gray07 = Color(0xFFE3E6ED)
val Gray08 = Color(0xFFF2F3F6)
val Gray09 = Color(0xFFF8F9FC)
val WHITE = Color(0xFFFFFFFF)

val defaultClodyColors = ClodyColors(
    kakaoYellow = KakaoYellow,
    appleBlack = AppleBlack,
    red = Red,
    lightGreenBack = LightGreenBack,
    lightGreen = LightGreen,
    mainGreen = MainGreen,
    darkGreen = DarkGreen,
    lightYellow = LightYellow,
    mainYellow = MainYellow,
    darkYellow = DarkYellow,
    lightBlue = LightBlue,
    blue = Blue,
    gray01 = Gray01,
    gray02 = Gray02,
    gray03 = Gray03,
    gray04 = Gray04,
    gray05 = Gray05,
    gray06 = Gray06,
    gray07 = Gray07,
    gray08 = Gray08,
    gray09 = Gray09,
    white = WHITE
)

@Immutable
data class ClodyColors(
    val kakaoYellow: Color,
    val appleBlack: Color,
    val red: Color,
    val lightGreenBack: Color,
    val lightGreen: Color,
    val mainGreen: Color,
    val darkGreen: Color,
    val lightYellow: Color,
    val mainYellow: Color,
    val darkYellow: Color,
    val lightBlue: Color,
    val blue: Color,
    val gray01: Color,
    val gray02: Color,
    val gray03: Color,
    val gray04: Color,
    val gray05: Color,
    val gray06: Color,
    val gray07: Color,
    val gray08: Color,
    val gray09: Color,
    val white: Color
)

val LocalClodyColors = staticCompositionLocalOf {
    ClodyColors(
        kakaoYellow = KakaoYellow,
        appleBlack = AppleBlack,
        red = Red,
        lightGreenBack = LightGreenBack,
        lightGreen = LightGreen,
        mainGreen = MainGreen,
        darkGreen = DarkGreen,
        lightYellow = LightYellow,
        mainYellow = MainYellow,
        darkYellow = DarkYellow,
        lightBlue = LightBlue,
        blue = Blue,
        gray01 = Gray01,
        gray02 = Gray02,
        gray03 = Gray03,
        gray04 = Gray04,
        gray05 = Gray05,
        gray06 = Gray06,
        gray07 = Gray07,
        gray08 = Gray08,
        gray09 = Gray09,
        white = WHITE
    )
}

val LocalClodyColors = staticCompositionLocalOf { defaultClodyColors }