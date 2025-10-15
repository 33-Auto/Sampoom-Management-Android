package com.sampoom.android.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.sampoom.android.R

val GmarketSansFamily = FontFamily(
    Font(R.font.gmarket_sans_light, FontWeight.Light),
    Font(R.font.gmarket_sans_medium, FontWeight.Medium),
    Font(R.font.gmarket_sans_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
private val baseTypography = Typography()
val Typography = Typography(
    displayLarge = baseTypography.displayLarge.copy(fontFamily = GmarketSansFamily),
    displayMedium = baseTypography.displayMedium.copy(fontFamily = GmarketSansFamily),
    displaySmall = baseTypography.displaySmall.copy(fontFamily = GmarketSansFamily),
    headlineLarge = baseTypography.headlineLarge.copy(fontFamily = GmarketSansFamily),
    headlineMedium = baseTypography.headlineMedium.copy(fontFamily = GmarketSansFamily),
    headlineSmall = baseTypography.headlineSmall.copy(fontFamily = GmarketSansFamily),
    titleLarge = baseTypography.titleLarge.copy(fontFamily = GmarketSansFamily),
    titleMedium = baseTypography.titleMedium.copy(fontFamily = GmarketSansFamily),
    titleSmall = baseTypography.titleSmall.copy(fontFamily = GmarketSansFamily),
    bodyLarge = baseTypography.bodyLarge.copy(fontFamily = GmarketSansFamily),
    bodyMedium = baseTypography.bodyMedium.copy(fontFamily = GmarketSansFamily),
    bodySmall = baseTypography.bodySmall.copy(fontFamily = GmarketSansFamily),
    labelLarge = baseTypography.labelLarge.copy(fontFamily = GmarketSansFamily),
    labelMedium = baseTypography.labelMedium.copy(fontFamily = GmarketSansFamily),
    labelSmall = baseTypography.labelSmall.copy(fontFamily = GmarketSansFamily)
)