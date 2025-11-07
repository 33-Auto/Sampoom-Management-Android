package com.sampoom.android.feature.auth.domain.usecase

import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ClearTokensUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.clearTokens()
}