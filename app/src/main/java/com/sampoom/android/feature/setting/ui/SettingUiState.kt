package com.sampoom.android.feature.setting.ui

import com.sampoom.android.feature.auth.domain.model.User

data class SettingUiState(
    val profile: User? = null,
    val userName: String = "",
    val userNameError: String? = null,

    val loading: Boolean = false,
    val error: Boolean = false,
    val profileChangeSuccess: Boolean = false,
    val logoutSuccess: Boolean = false
) {
    val isValid: Boolean
        get() = userName.isNotBlank() && userNameError == null
}