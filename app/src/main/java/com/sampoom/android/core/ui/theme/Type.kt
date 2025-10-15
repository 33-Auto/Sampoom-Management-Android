package com.sampoom.android.core.ui.theme

import android.R.attr.fontFamily
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.sampoom.android.R

val GmarketSansFamily = FontFamily(
    Font(R.font.gmarket_sans_light, FontWeight.Light),
    Font(R.font.gmarket_sans_medium, FontWeight.Normal),
    Font(R.font.gmarket_sans_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = GmarketSansFamily),
    displayMedium = TextStyle(fontFamily = GmarketSansFamily),
    displaySmall = TextStyle(fontFamily = GmarketSansFamily),
    headlineLarge = TextStyle(fontFamily = GmarketSansFamily),
    headlineMedium = TextStyle(fontFamily = GmarketSansFamily),
    headlineSmall = TextStyle(fontFamily = GmarketSansFamily),
    titleLarge = TextStyle(fontFamily = GmarketSansFamily),
    titleMedium = TextStyle(fontFamily = GmarketSansFamily),
    titleSmall = TextStyle(fontFamily = GmarketSansFamily),
    bodyLarge = TextStyle(fontFamily = GmarketSansFamily),
    bodyMedium = TextStyle(fontFamily = GmarketSansFamily),
    bodySmall = TextStyle(fontFamily = GmarketSansFamily),
    labelLarge = TextStyle(fontFamily = GmarketSansFamily),
    labelMedium = TextStyle(fontFamily = GmarketSansFamily),
    labelSmall = TextStyle(fontFamily = GmarketSansFamily)
)