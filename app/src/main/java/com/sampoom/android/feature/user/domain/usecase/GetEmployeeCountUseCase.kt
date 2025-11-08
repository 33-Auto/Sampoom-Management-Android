package com.sampoom.android.feature.user.domain.usecase

import com.sampoom.android.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class GetEmployeeCountUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<Int> = repository.getEmployeeCount()
}