package com.sampoom.android.feature.dashboard.domain.usecase

import com.sampoom.android.feature.dashboard.domain.model.WeeklySummary
import com.sampoom.android.feature.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject

class WeeklySummaryUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): Result<WeeklySummary> = repository.getWeeklySummary()
}