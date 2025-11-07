package com.sampoom.android.feature.outbound.domain.usecase

import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import javax.inject.Inject

class AddOutboundUseCase @Inject constructor(
    private val repository: OutboundRepository
) {
    suspend operator fun invoke(partId: Long, quantity: Long): Result<Unit> = repository.addOutbound(partId, quantity)
}