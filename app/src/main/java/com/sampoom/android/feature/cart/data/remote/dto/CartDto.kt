package com.sampoom.android.feature.cart.data.remote.dto

data class CartDto(
    val categoryId: Long,
    val categoryName: String,
    val groups: List<CartGroupDto>
)

data class CartGroupDto(
    val groupId: Long,
    val groupName: String,
    val parts: List<CartPartDto>
)

data class CartPartDto(
    val cartItemId: Long,
    val partId: Long,
    val code: String,
    val name: String,
    val quantity: Long
)