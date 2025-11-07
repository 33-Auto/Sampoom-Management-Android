package com.sampoom.android.feature.dashboard.domain.repository

import com.sampoom.android.feature.dashboard.domain.model.Dashboard
import com.sampoom.android.feature.dashboard.domain.model.WeeklySummary

interface DashboardRepository {
    suspend fun getDashboard(): Result<Dashboard>
    suspend fun getWeeklySummary(): Result<WeeklySummary>
}