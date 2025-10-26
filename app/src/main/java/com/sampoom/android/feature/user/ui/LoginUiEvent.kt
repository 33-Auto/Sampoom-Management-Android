package com.sampoom.android.feature.user.ui

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object Submit: LoginUiEvent
}