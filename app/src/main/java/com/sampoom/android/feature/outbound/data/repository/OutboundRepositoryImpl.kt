package com.sampoom.android.feature.outbound.data.repository

import com.sampoom.android.feature.outbound.data.mapper.toModel
import com.sampoom.android.feature.outbound.data.remote.api.OutboundApi
import com.sampoom.android.feature.outbound.data.remote.dto.AddOutboundRequestDto
import com.sampoom.android.feature.outbound.data.remote.dto.UpdateOutboundRequestDto
import com.sampoom.android.feature.outbound.domain.model.OutboundList
import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import jakarta.inject.Inject

class OutboundRepositoryImpl @Inject constructor(
    private val api: OutboundApi
) : OutboundRepository {
    override suspend fun getOutboundList(): OutboundList {
        val dto = api.getOutboundList()
        val outboundItems = dto.data.map { it.toModel() }
        return OutboundList(items = outboundItems)
    }

    override suspend fun processOutbound(): Result<Unit> {
        return runCatching {
            val dto = api.processOutbound()
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun addOutbound(
        partId: Long,
        quantity: Long
    ): Result<Unit> {
        return runCatching {
            val dto = api.addOutbound(AddOutboundRequestDto(partId, quantity))
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun deleteOutbound(outboundId: Long): Result<Unit> {
        return runCatching {
            val dto = api.deleteOutbound(outboundId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun deleteAllOutbound(): Result<Unit> {
        return runCatching {
            val dto = api.deleteAllOutbound()
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun updateOutboundQuantity(
        outboundId: Long,
        quantity: Long
    ): Result<Unit> {
        return runCatching {
            val dto = api.updateOutbound(outboundId, UpdateOutboundRequestDto(quantity))
            if (!dto.success) throw Exception(dto.message)
        }
    }
}