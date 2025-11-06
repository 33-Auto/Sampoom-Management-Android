package com.sampoom.android.feature.outbound.data.mapper

import com.sampoom.android.feature.outbound.data.remote.dto.OutboundDto
import com.sampoom.android.feature.outbound.data.remote.dto.OutboundGroupDto
import com.sampoom.android.feature.outbound.data.remote.dto.OutboundPartDto
import com.sampoom.android.feature.outbound.domain.model.Outbound
import com.sampoom.android.feature.outbound.domain.model.OutboundGroup
import com.sampoom.android.feature.outbound.domain.model.OutboundPart

fun OutboundDto.toModel(): Outbound = Outbound(categoryId, categoryName, groups.map { it.toModel() })
fun OutboundGroupDto.toModel(): OutboundGroup = OutboundGroup(groupId, groupName, parts.map { it.toModel() })
fun OutboundPartDto.toModel(): OutboundPart = OutboundPart(outboundId, partId, code, name, quantity, standardCost)