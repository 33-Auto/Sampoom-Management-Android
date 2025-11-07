package com.sampoom.android.feature.dashboard.data.mapper

import com.sampoom.android.feature.dashboard.data.remote.dto.DashboardResponseDto
import com.sampoom.android.feature.dashboard.data.remote.dto.WeeklySummaryResponseDto
import com.sampoom.android.feature.dashboard.domain.model.Dashboard
import com.sampoom.android.feature.dashboard.domain.model.WeeklySummary

fun DashboardResponseDto.toModel(): Dashboard = Dashboard(totalParts, outOfStockParts, lowStockParts, totalQuantity)
fun WeeklySummaryResponseDto.toModel(): WeeklySummary = WeeklySummary(inStockParts, outStockParts, weekPeriod)