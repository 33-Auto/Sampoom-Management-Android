package com.sampoom.android.feature.auth.domain.usecase

import com.sampoom.android.core.preferences.AuthPreferences
import com.sampoom.android.feature.auth.domain.model.User
import javax.inject.Inject

class GetStoredUserUseCase @Inject constructor(
    private val preferences: AuthPreferences
) {
    suspend operator fun invoke(): User? = preferences.getStoredUser()
}



