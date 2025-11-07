package com.sampoom.android.feature.outbound.data.repository

import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.outbound.data.mapper.toModel
import com.sampoom.android.feature.outbound.data.remote.api.OutboundApi
import com.sampoom.android.feature.outbound.data.remote.dto.AddOutboundRequestDto
import com.sampoom.android.feature.outbound.data.remote.dto.UpdateOutboundRequestDto
import com.sampoom.android.feature.outbound.domain.model.OutboundList
import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import jakarta.inject.Inject

class OutboundRepositoryImpl @Inject constructor(
    private val api: OutboundApi,
    private val authPreferences: AuthPreferences
) : OutboundRepository {
    override suspend fun getOutboundList(): Result<OutboundList> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.getOutboundList(agencyId)
            val outboundItems = dto.data.map { it.toModel() }
            OutboundList(items = outboundItems)
        }
    }

    override suspend fun processOutbound(): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.processOutbound(agencyId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun addOutbound(
        partId: Long,
        quantity: Long
    ): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto =
                api.addOutbound(agencyId = agencyId, body = AddOutboundRequestDto(partId, quantity))
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun deleteOutbound(outboundId: Long): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.deleteOutbound(agencyId = agencyId, outboundId = outboundId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun deleteAllOutbound(): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.deleteAllOutbound(agencyId)
            if (!dto.success) throw Exception(dto.message)
        }
    }

    override suspend fun updateOutboundQuantity(
        outboundId: Long,
        quantity: Long
    ): Result<Unit> {
        return runCatching {
            val agencyId = authPreferences.getStoredUser()?.agencyId ?: throw Exception()
            val dto = api.updateOutbound(
                agencyId = agencyId,
                outboundId = outboundId,
                body = UpdateOutboundRequestDto(quantity)
            )
            if (!dto.success) throw Exception(dto.message)
        }
    }
}