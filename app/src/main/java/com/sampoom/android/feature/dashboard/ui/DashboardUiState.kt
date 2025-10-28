package com.sampoom.android.feature.dashboard.ui

import com.sampoom.android.feature.order.domain.model.Order

data class DashboardUiState(
    val orderList: List<Order> = emptyList(),
    val dashboardLoading: Boolean = false,
    val dashboardError: String? = null,
)