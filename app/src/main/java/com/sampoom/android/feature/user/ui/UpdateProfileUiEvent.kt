package com.sampoom.android.feature.user.ui

import com.sampoom.android.feature.user.domain.model.User

interface UpdateProfileUiEvent {
    data class Initialize(val user: User) : UpdateProfileUiEvent
    data class UpdateProfile(val userName: String) : UpdateProfileUiEvent
    object Dismiss : UpdateProfileUiEvent
}