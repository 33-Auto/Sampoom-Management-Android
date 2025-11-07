package com.sampoom.android.feature.dashboard.ui

sealed interface SettingUiEvent {
    object LoadProfile : SettingUiEvent
    data class NameChanged(val userName: String) : SettingUiEvent
}