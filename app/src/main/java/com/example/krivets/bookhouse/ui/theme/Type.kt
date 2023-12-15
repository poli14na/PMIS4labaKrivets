package com.example.krivets.bookhouse.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.krivets.bookhouse.R

val montserrat = FontFamily(
        Font(R.font.montserrat, FontWeight.Light)
)
// Set of Material typography styles to start with


val Typography = Typography(

    bodySmall = TextStyle(
        fontFamily = montserrat,
        fontSize = 12.sp,
        textAlign = TextAlign.Center
    ),

     bodyMedium = TextStyle(
         fontFamily = montserrat,
         fontSize = 13.sp,

     ),

    bodyLarge = TextStyle(
        fontFamily = montserrat,
        fontSize = 13.sp,
        textAlign = TextAlign.Center
    ),

    labelMedium = TextStyle(
        fontFamily = montserrat,
        fontSize = 15.sp,
        textAlign = TextAlign.Center
    ),

    labelLarge = TextStyle(
        fontFamily = montserrat,
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    ),

    titleLarge = TextStyle(
        fontFamily = montserrat,
        fontSize = 27.sp,
        textAlign = TextAlign.Center,


    ),

    labelSmall = TextStyle(
        fontSize = 17.sp,
        fontFamily = montserrat,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold
        ),

)
