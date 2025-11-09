package com.sampoom.android.feature.auth.domain.usecase

import com.sampoom.android.feature.auth.domain.repository.AuthRepository
import com.sampoom.android.feature.user.domain.model.User
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        workspace: String,
        branch: String,
        userName: String,
        position: String
    ): Result<User> = repository.signUp(
        email = email,
        password = password,
        workspace = workspace,
        branch = branch,
        userName = userName,
        position = position
    )
}