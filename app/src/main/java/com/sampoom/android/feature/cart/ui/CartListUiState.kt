package com.sampoom.android.feature.cart.ui

import com.sampoom.android.feature.cart.domain.model.Cart
import com.sampoom.android.feature.order.domain.model.Order

data class CartListUiState(
    val cartList: List<Cart> = emptyList(),
    val cartLoading: Boolean = false,
    val cartError: String? = null,
    val selectedCart: Cart? = null,
    val isUpdating: Boolean = false,
    val updateError: String? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null,
    val isProcessing: Boolean = false,
    val processError: String? = null,
    val processedOrder: Order? = null
)
