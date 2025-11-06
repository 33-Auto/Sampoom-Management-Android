package com.sampoom.android.feature.dashboard.domain.model

data class Dashboard(
    val totalParts: Long,
    val outOfStockParts: Long,
    val lowStockParts: Long,
    val totalQuantity: Long
)
