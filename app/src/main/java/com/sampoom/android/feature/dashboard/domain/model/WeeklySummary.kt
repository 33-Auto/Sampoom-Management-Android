package com.sampoom.android.feature.dashboard.domain.model

data class WeeklySummary(
    val inStockParts: Long,
    val outStockParts: Long,
    val weekPeriod: String
)