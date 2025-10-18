package com.sampoom.android.feature.part.ui

sealed interface PartListUiEvent {
    object LoadPartList : PartListUiEvent
    object RetryPartList : PartListUiEvent
}