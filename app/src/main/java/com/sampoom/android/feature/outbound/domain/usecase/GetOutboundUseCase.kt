package com.sampoom.android.feature.outbound.domain.usecase

import com.sampoom.android.feature.outbound.domain.model.OutboundList
import com.sampoom.android.feature.outbound.domain.repository.OutboundRepository
import javax.inject.Inject

class GetOutboundUseCase @Inject constructor(
    private val repository: OutboundRepository
) {
    suspend operator fun invoke(): Result<OutboundList> = repository.getOutboundList()
}