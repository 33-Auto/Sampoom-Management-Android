package com.sampoom.android.feature.order.ui

sealed interface OrderDetailUiEvent {
    object LoadOrder : OrderDetailUiEvent
    object RetryOrder : OrderDetailUiEvent
}