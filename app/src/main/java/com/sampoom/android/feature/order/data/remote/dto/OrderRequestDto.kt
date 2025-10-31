package com.sampoom.android.feature.order.data.remote.dto

data class OrderRequestDto(
    val requester: String,
    val branch: String,
    val items: List<OrderItems>
)

data class OrderItems(
    val code: String,
    val quantity: Long
)