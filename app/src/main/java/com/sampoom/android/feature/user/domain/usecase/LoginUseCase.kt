package com.sampoom.android.feature.user.domain.usecase

import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): User =
        repository.signIn(email, password)
}