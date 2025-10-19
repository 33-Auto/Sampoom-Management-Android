package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Part

sealed interface PartDetailUiEvent {
    data class Initialize(val part: Part) : PartDetailUiEvent
    object IncreaseQuantity : PartDetailUiEvent
    object DecreaseQuantity : PartDetailUiEvent
    data class SetQuantity(val quantity: Long) : PartDetailUiEvent
    object UpdateQuantity : PartDetailUiEvent
    object ClearError : PartDetailUiEvent
    object Dismiss : PartDetailUiEvent
}