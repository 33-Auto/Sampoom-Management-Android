package com.sampoom.android.feature.dashboard.data.remote.dto

data class WeeklySummaryResponseDto(
    val inStockParts: Long,
    val outStockParts: Long,
    val weekPeriod: String
)
