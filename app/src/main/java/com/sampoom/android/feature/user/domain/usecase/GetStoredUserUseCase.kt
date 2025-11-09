package com.sampoom.android.feature.user.domain.usecase

import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.domain.repository.UserRepository
import javax.inject.Inject

class GetStoredUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): User? = repository.getStoredUser()
}