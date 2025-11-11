package com.sampoom.android.feature.order.data.remote.dto

data class OrderRequestDto(
    val agencyId: Long,
    val agencyName: String, // branch
    val items: List<OrderCategoryDto>
)