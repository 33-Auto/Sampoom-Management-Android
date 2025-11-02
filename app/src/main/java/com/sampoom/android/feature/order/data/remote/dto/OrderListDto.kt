package com.sampoom.android.feature.order.data.remote.dto

data class OrderListDto(
    val content: List<OrderDto>,
    val totalElements: Int,
    val totalPages: Int,
    val currentPage: Int
)
