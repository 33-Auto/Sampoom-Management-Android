package com.sampoom.android.feature.outbound.ui

sealed interface OutboundListUiEvent {
    object LoadOutboundList : OutboundListUiEvent
    object RetryOutboundList : OutboundListUiEvent
    data class UpdateQuantity(val outboundId: Long, val quantity: Long) : OutboundListUiEvent
    data class DeleteOutbound(val outboundId: Long) : OutboundListUiEvent
    object ClearUpdateError : OutboundListUiEvent
    object ClearDeleteError : OutboundListUiEvent
}