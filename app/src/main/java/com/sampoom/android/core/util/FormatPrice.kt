package com.sampoom.android.core.util

import java.text.NumberFormat
import java.util.Locale

fun formatWon(value: Long): String =
    NumberFormat.getInstance(Locale.KOREA).format(value) + "원"