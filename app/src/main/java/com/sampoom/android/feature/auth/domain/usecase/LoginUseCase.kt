package com.sampoom.android.feature.auth.domain.usecase

import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import com.sampoom.android.feature.user.domain.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> = repository.signIn(email, password)
}