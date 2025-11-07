package com.sampoom.android.feature.dashboard.data.remote.dto

data class DashboardResponseDto(
    val totalParts: Long,
    val outOfStockParts: Long,
    val lowStockParts: Long,
    val totalQuantity: Long
)
