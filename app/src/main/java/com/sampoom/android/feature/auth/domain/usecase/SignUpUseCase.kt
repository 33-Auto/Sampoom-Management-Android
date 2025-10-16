package com.sampoom.android.feature.auth.domain.usecase

import com.sampoom.android.feature.auth.domain.model.User
import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        workspace: String,
        branch: String,
        position: String,
        email: String,
        password: String
    ): User = repository.signUp(
        name = name,
        workspace = workspace,
        branch = branch,
        position = position,
        email = email,
        password = password
    )
}