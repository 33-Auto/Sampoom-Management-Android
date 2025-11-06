package com.sampoom.android.feature.dashboard.data.repository

import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.dashboard.data.mapper.toModel
import com.sampoom.android.feature.dashboard.data.remote.api.DashboardApi
import com.sampoom.android.feature.dashboard.domain.model.Dashboard
import com.sampoom.android.feature.dashboard.domain.model.WeeklySummary
import com.sampoom.android.feature.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: DashboardApi,
    private val authPreferences: AuthPreferences
) : DashboardRepository {
    override suspend fun getDashboard(): Result<Dashboard> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.getDashboard(agencyId)
            dto.data.toModel()
        }
    }

    override suspend fun getWeeklySummary(): Result<WeeklySummary> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.getWeeklySummary(agencyId)
            dto.data.toModel()
        }
    }
}