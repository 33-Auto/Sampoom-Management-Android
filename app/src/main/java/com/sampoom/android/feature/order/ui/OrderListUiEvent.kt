package com.sampoom.android.feature.order.ui

sealed interface OrderListUiEvent {
    object LoadOrderList : OrderListUiEvent
    object RetryOrderList : OrderListUiEvent
}