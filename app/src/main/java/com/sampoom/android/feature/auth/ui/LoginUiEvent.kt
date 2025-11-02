package com.sampoom.android.feature.auth.ui

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object Submit: LoginUiEvent
}