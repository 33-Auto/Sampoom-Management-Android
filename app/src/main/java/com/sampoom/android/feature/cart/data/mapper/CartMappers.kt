package com.sampoom.android.feature.cart.data.mapper

import com.sampoom.android.feature.cart.data.remote.dto.CartDto
import com.sampoom.android.feature.cart.data.remote.dto.CartGroupDto
import com.sampoom.android.feature.cart.data.remote.dto.CartPartDto
import com.sampoom.android.feature.cart.domain.model.Cart
import com.sampoom.android.feature.cart.domain.model.CartGroup
import com.sampoom.android.feature.cart.domain.model.CartPart

fun CartDto.toModel(): Cart = Cart(categoryId, categoryName, groups.map { it.toModel() })
fun CartGroupDto.toModel(): CartGroup = CartGroup(groupId, groupName, parts.map { it.toModel() })
fun CartPartDto.toModel(): CartPart = CartPart(cartItemId, partId, code, name, quantity)