package com.sampoom.android.feature.auth.ui

import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.auth.domain.model.Vendor

data class SignUpUiState(
    val name: String = "",
    val workspace: String = "AGENCY",
    val branch: String = "",
    val position: UserPosition? = null,
    val email: String = "",
    val password: String = "",
    val passwordCheck: String = "",

    // Vendor
    val vendors: List<Vendor> = emptyList(),
    val selectedVendor: Vendor? = null,
    val vendorsLoading: Boolean = false,

    // Error message
    val nameError: String? = null,
    val branchError: String? = null,
    val positionError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordCheckError: String? = null,

    val loading: Boolean = false,
    val success: Boolean = false
) {
    val isValid: Boolean
        get() = name.isNotBlank() &&
                branch.isNotBlank() &&
                position != null &&
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