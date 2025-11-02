package com.sampoom.android.feature.order.data.remote.dto

data class OrderRequestDto(
    val agencyName: String, // branch
    val items: List<OrderCategoryDto>
)