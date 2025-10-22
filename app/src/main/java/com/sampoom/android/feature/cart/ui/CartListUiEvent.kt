package com.sampoom.android.feature.cart.ui

sealed interface CartListUiEvent {
    object LoadCartList : CartListUiEvent
    object RetryCartList : CartListUiEvent
    object ProcessOrder : CartListUiEvent
    data class UpdateQuantity(val cartItemId: Long, val quantity: Long) : CartListUiEvent
    data class DeleteCart(val cartItemId: Long) : CartListUiEvent
    object DeleteAllCart : CartListUiEvent
    object ClearUpdateError : CartListUiEvent
    object ClearDeleteError : CartListUiEvent
    object DismissOrderResult : CartListUiEvent
}