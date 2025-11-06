package com.sampoom.android.feature.dashboard.ui

import com.sampoom.android.feature.dashboard.domain.model.Dashboard
import com.sampoom.android.feature.dashboard.domain.model.WeeklySummary
import com.sampoom.android.feature.order.domain.model.Order

data class DashboardUiState(
    val orderList: List<Order> = emptyList(),
    val dashboard: Dashboard? = null,
    val weeklySummary: WeeklySummary? = null,
    val dashboardLoading: Boolean = false,
    val dashboardError: String? = null
)