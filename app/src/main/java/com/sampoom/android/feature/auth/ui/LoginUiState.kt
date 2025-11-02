package com.sampoom.android.feature.auth.ui

data class LoginUiState(
    val email: String = "",
    val password: String = "",

    // Error message
    val emailError: String? = null,
    val passwordError: String? = null,

    val loading: Boolean = false,
    val success: Boolean = false
) {
    val isValid: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                emailError == null &&
                passwordError == null
}
