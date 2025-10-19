package com.sampoom.android.feature.part.ui

import com.sampoom.android.feature.part.domain.model.Part

sealed interface PartDetailUiEvent {
    data class Initialize(val part: Part) : PartDetailUiEvent
    object IncreaseQuantity : PartDetailUiEvent
    object DecreaseQuantity : PartDetailUiEvent
    data class SetQuantity(val quantity: Long) : PartDetailUiEvent
    data class AddToOutbound(val partId: Long, val quantity: Long) : PartDetailUiEvent
    object ClearError : PartDetailUiEvent
    object Dismiss : PartDetailUiEvent
}