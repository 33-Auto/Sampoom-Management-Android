package com.sampoom.android.feature.dashboard.ui

sealed interface DashboardUiEvent {
    object LoadDashboard : DashboardUiEvent
    object RetryDashboard : DashboardUiEvent
}