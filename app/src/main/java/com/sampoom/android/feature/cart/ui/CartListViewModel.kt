package com.sampoom.android.feature.cart.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.cart.domain.model.CartList
import com.sampoom.android.feature.cart.domain.usecase.DeleteAllCartUseCase
import com.sampoom.android.feature.cart.domain.usecase.DeleteCartUseCase
import com.sampoom.android.feature.cart.domain.usecase.GetCartUseCase
import com.sampoom.android.feature.cart.domain.usecase.UpdateCartQuantityUseCase
import com.sampoom.android.feature.order.domain.usecase.CreateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartListViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val getCartListUseCase: GetCartUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val deleteAllCartUseCase: DeleteAllCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "CartListViewModel"
    }

    private val _uiState = MutableStateFlow(CartListUiState())
    val uiState: StateFlow<CartListUiState> = _uiState

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        loadCartList()
    }

    fun onEvent(event: CartListUiEvent) {
        when (event) {
            is CartListUiEvent.LoadCartList -> loadCartList()
            is CartListUiEvent.RetryCartList -> loadCartList()
            is CartListUiEvent.ProcessOrder -> processOrder()
            is CartListUiEvent.UpdateQuantity -> updateQuantity(event.cartItemId, event.quantity)
            is CartListUiEvent.DeleteCart -> deleteCart(event.cartItemId)
            is CartListUiEvent.DeleteAllCart -> deleteAllCart()
            is CartListUiEvent.DismissOrderResult -> _uiState.update { it.copy(processedOrder = null) }
        }
    }

    private fun loadCartList() {
        viewModelScope.launch {
            _uiState.update { it.copy(cartLoading = true, cartError = null) }

            getCartListUseCase()
                .onSuccess { cartList ->
                    _uiState.update {
                        it.copy(
                            cartList = cartList.items,
                            cartLoading = false,
                            cartError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            cartLoading = false,
                            cartError = error
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun processOrder() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, processError = null) }

            val cartList = CartList(items = _uiState.value.cartList)
            createOrderUseCase(cartList)
                .onSuccess { order ->
                    _uiState.update { it.copy(isProcessing = false, processedOrder = order) }
                    loadCartList()
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            processError = error
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun updateQuantity(cartItemId: Long, newQuantity: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, updateError = null) }

            updateCartQuantityUseCase(cartItemId, newQuantity)
                .onSuccess {
                    _uiState.update { it.copy(isUpdating = false) }
                    updateLocalQuantity(cartItemId, newQuantity)
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            updateError = error
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun updateLocalQuantity(cartItemId: Long, newQuantity: Long) {
        _uiState.update { currentState ->
            val updatedList = currentState.cartList.map { category ->
                category.copy(
                    groups = category.groups.map { group ->
                        group.copy(
                            parts = group.parts.map { part ->
                                if (part.cartItemId == cartItemId) {
                                    part.copy(quantity = newQuantity)
                                } else {
                                    part
                                }
                            }
                        )
                    }
                )
            }
            currentState.copy(cartList = updatedList)
        }
    }

    private fun deleteCart(cartItemId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, deleteError = null) }

            deleteCartUseCase(cartItemId)
                .onSuccess {
                    _uiState.update { it.copy(isDeleting = false) }
                    removeFromLocalList(cartItemId)
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            deleteError = error
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun deleteAllCart() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, deleteError = null) }

            deleteAllCartUseCase()
                .onSuccess {
                    _uiState.update { it.copy(isDeleting = false) }
                    removeAllFromLocalList()
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            deleteError = error
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun removeFromLocalList(cartItemId: Long) {
        _uiState.update { currentState ->
            val updatedList = currentState.cartList.map { category ->
                category.copy(
                    groups = category.groups.map { group ->
                        group.copy(
                            parts = group.parts.filter { part ->
                                part.cartItemId != cartItemId
                            }
                        )
                    }.filter { group ->
                        group.parts.isNotEmpty()
                    }
                )
            }.filter { category ->
                category.groups.isNotEmpty()
            }
            currentState.copy(cartList = updatedList)
        }
    }

    private fun removeAllFromLocalList() {
        _uiState.update { currentState ->
            currentState.copy(cartList = emptyList())
        }
    }
}