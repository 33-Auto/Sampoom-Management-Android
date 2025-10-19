package com.sampoom.android.feature.cart.domain.model

data class Cart(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<CartGroup>
)

data class CartGroup(
    val groupId: Long,
    val groupName: String,
    val parts: List<CartPart>
)

data class CartPart(
    val cartItemId: Long,
    val partId: Long,
    val code: String,
    val name: String,
    val quantity: Long
)