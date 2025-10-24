package com.sampoom.android.feature.user.ui

data class SignUpUiState(
    val name: String = "",
    val workspace: String = "대리점",
    val branch: String = "",
    val position: String = "",
    val email: String = "",
    val password: String = "",
    val passwordCheck: String = "",

    // Error message
    val nameError: String? = null,
    val branchError: String? = null,
    val positionError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordCheckError: String? = null,

    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotBlank() &&
                branch.isNotBlank() &&
                position.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                passwordCheck.isNotBlank() &&
                nameError == null &&
                branchError == null &&
                positionError == null &&
                emailError == null &&
                passwordError == null &&
                passwordCheckError == null
}