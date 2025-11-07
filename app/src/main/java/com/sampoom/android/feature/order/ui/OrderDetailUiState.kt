package com.sampoom.android.feature.order.ui

import com.sampoom.android.feature.order.domain.model.Order

data class OrderDetailUiState(
    val orderDetail: Order? = null,
    val orderDetailLoading: Boolean = false,
    val orderDetailError: String? = null,
    val isProcessing: Boolean = false,
    val isProcessingCancelSuccess: Boolean = false,
    val isProcessingReceiveSuccess: Boolean = false,
)