package com.example.krivets.bookhouse.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val PinkLight80 = Color(0xFFFCEDF3)
val Purple100 = Color(0xFF1E0620)

val Purple90 = Color(0xFF5F2B3F)
val Purple40 = Color(0xFFAD5472)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFFD5CDD2)

val Red40 = Color(0xFFE86666)
val Green40 = Color(0xFF50A66D)
val DaySelectedTextColor = Color(0xFF9F5771)
val NightSelectedTextColor = Color(0xFF8766CC)

val Gray90 = Color(0xFF8D8B8E)


sealed class ThemeColors(
    val background: Color,
    val primary: Color,
    val text: Color,
    val surface:  Color,
    val onSurface: Color,
){
    object Night: ThemeColors(
        background = Color(0xFF1E0620),
        primary = NightSelectedTextColor,
        text = Color.White,
        surface = Color(0xFF1E0620),
        onSurface = NightSelectedTextColor,
    )

    object Day: ThemeColors(
        background = Color(0xFFFCEDF3),
        primary = DaySelectedTextColor,
        text = Color.Black,
        surface = Color(0xFFFCEDF3),
        onSurface = DaySelectedTextColor,
    )
}

