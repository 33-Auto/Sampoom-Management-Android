package com.sampoom.android.feature.user.domain.usecase

import com.sampoom.android.core.datastore.AuthPreferences
import com.sampoom.android.feature.user.domain.model.User
import javax.inject.Inject

class GetStoredUserUseCase @Inject constructor(
    private val preferences: AuthPreferences
) {
    suspend operator fun invoke(): User? = preferences.getStoredUser()
}



