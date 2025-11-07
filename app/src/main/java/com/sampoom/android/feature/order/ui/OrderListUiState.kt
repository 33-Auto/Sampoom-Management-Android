package com.sampoom.android.feature.order.ui

import com.sampoom.android.feature.order.domain.model.Order

data class OrderListUiState(
    val orderList: List<Order> = emptyList(),
    val orderLoading: Boolean = false,
    val orderError: String? = null
)