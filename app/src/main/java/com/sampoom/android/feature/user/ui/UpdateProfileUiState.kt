package com.sampoom.android.feature.user.ui

import com.sampoom.android.feature.user.domain.model.User

data class UpdateProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)