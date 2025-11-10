package com.sampoom.android.feature.auth.ui

import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.feature.auth.domain.model.Vendor

sealed interface SignUpUiEvent {
    data class NameChanged(val name: String) : SignUpUiEvent
    data class VendorChanged(val vendor: Vendor) : SignUpUiEvent
    data class PositionChanged(val position: UserPosition) : SignUpUiEvent
    data class EmailChanged(val email: String) : SignUpUiEvent
    data class PasswordChanged(val password: String) : SignUpUiEvent
    data class PasswordCheckChanged(val passwordCheck: String) : SignUpUiEvent
    data object Submit: SignUpUiEvent
}