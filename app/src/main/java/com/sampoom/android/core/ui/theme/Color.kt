package com.sampoom.android.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

val SuccessGreen = Color(0xFF10B981)
val FailRed = Color(0xFFFF6C6C)
val WaitYellow = Color(0xFFF59E0B)

val Grey400 = Color(0xFF444444)
val Grey300 = Color(0xFF7C7C7C)
val Grey200 = Color(0xFFCCCCCC)
val Grey100 = Color(0xFFE9EAEC)

val BgWhite = Color(0xFFF5F5F5)
val BgCardWhite = Color(0xFFFFFFFF)
val BgBlack = Color(0xFF17181B)
val BgCardBlack = Color(0xFF36393F)

val Main900 = Color(0xFF1F1F5C)
val Main800 = Color(0xFF333399)
val Main700 = Color(0xFF4C4CBB)
val Main600 = Color(0xFF6666DD)
val Main500 = Color(0xFF8080FF)
val Main400 = Color(0xFF9999FF)
val Main300 = Color(0xFFB3B3FF)
val Main200 = Color(0xFFCCCCFF)
val Main100 = Color(0xFFE6E6FF)

@Composable
fun backgroundColor() = if (isSystemInDarkTheme()) BgBlack else BgWhite

@Composable
fun backgroundCardColor() = if (isSystemInDarkTheme()) BgCardBlack else BgCardWhite

@Composable
fun textColor() = if (isSystemInDarkTheme()) BgWhite else BgCardBlack

@Composable
fun textSecondaryColor() = if (isSystemInDarkTheme()) Grey200 else Grey300

@Composable
fun disableColor() = if (isSystemInDarkTheme()) Grey400 else Grey100