package com.sampoom.android.feature.cart.ui

import com.sampoom.android.feature.cart.domain.model.Cart

data class CartListUiState(
    val cartList: List<Cart> = emptyList(),
    val cartLoading: Boolean = false,
    val cartError: String? = null,
    val selectedCart: Cart? = null,
    val isUpdating: Boolean = false,
    val updateError: String? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null,
    val isOrderSuccess: Boolean = false
)
