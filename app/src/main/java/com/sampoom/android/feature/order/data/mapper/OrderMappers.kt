package com.sampoom.android.feature.order.data.mapper

import com.sampoom.android.feature.order.data.remote.dto.OrderCategoryDto
import com.sampoom.android.feature.order.data.remote.dto.OrderDto
import com.sampoom.android.feature.order.data.remote.dto.OrderGroupDto
import com.sampoom.android.feature.order.data.remote.dto.OrderPartDto
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.model.OrderCategory
import com.sampoom.android.feature.order.domain.model.OrderGroup
import com.sampoom.android.feature.order.domain.model.OrderPart

fun OrderDto.toModel(): Order = Order(orderId, orderNumber, createdAt, status, agencyName, items.map { it.toModel() })
fun OrderCategoryDto.toModel(): OrderCategory = OrderCategory(categoryId, categoryName, groups.map { it.toModel() })
fun OrderGroupDto.toModel(): OrderGroup = OrderGroup(groupId, groupName, parts.map { it.toModel() })
fun OrderPartDto.toModel(): OrderPart = OrderPart(partId, code, name, quantity, standardCost)