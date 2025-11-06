package com.sampoom.android.feature.auth.domain.usecase

import com.sampoom.android.feature.auth.domain.model.VendorList
import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetVendorUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<VendorList> = repository.getVendorList()
}