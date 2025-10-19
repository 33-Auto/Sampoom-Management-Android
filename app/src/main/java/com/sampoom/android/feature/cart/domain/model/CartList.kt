package com.sampoom.android.feature.cart.domain.model

data class CartList(
    val items: List<Cart>,
    val totalCount: Int = items.size,
    val isEmpty: Boolean = items.isEmpty()
) {
    companion object Companion {
        fun empty() = CartList(emptyList())
    }
}
