package com.sampoom.android.feature.setting.ui

sealed interface SettingUiEvent {
    object LoadProfile : SettingUiEvent
    data class NameChanged(val userName: String) : SettingUiEvent
    object EditProfile : SettingUiEvent
}