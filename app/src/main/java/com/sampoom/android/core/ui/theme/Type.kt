package com.sampoom.android.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sampoom.android.R

val GmarketSansFamily = FontFamily(
    Font(R.font.gmarket_sans_light, FontWeight.Light),
    Font(R.font.gmarket_sans_medium, FontWeight.Medium),
    Font(R.font.gmarket_sans_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
private val baseTypography = Typography()
val Typography = Typography(
    displayLarge = baseTypography.displayLarge.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 43.sp
    ),
    displayMedium = baseTypography.displayMedium.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 34.sp
    ),
    displaySmall = baseTypography.displaySmall.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 27.sp
    ),
    headlineLarge = baseTypography.headlineLarge.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 24.sp
    ),
    headlineMedium = baseTypography.headlineMedium.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 21.sp
    ),
    headlineSmall = baseTypography.headlineSmall.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 18.sp
    ),
    titleLarge = baseTypography.titleLarge.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 16.sp
    ),
    titleMedium = baseTypography.titleMedium.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 12.sp
    ),
    titleSmall = baseTypography.titleSmall.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 11.sp
    ),
    bodyLarge = baseTypography.bodyLarge.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 12.sp
    ),
    bodyMedium = baseTypography.bodyMedium.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 12.sp
    ),
    bodySmall = baseTypography.bodySmall.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 10.sp
    ),
    labelLarge = baseTypography.labelLarge.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 11.sp
    ),
    labelMedium = baseTypography.labelMedium.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 10.sp
    ),
    labelSmall = baseTypography.labelSmall.copy(
        fontFamily = GmarketSansFamily,
        fontSize = 9.sp
    )
)