package com.sampoom.android.feature.user.domain.usecase

import com.sampoom.android.feature.user.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.signOut()
}