package com.sampoom.android.feature.part.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.cart.domain.usecase.AddCartUseCase
import com.sampoom.android.feature.outbound.domain.usecase.AddOutboundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PartDetailViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val addOutboundUseCase: AddOutboundUseCase,
    private val addCartUseCase: AddCartUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "OutboundDetailViewModel"
    }

    private val _uiState = MutableStateFlow(PartDetailUiState())
    val uiState: StateFlow<PartDetailUiState> = _uiState

    private var errorLabel: String = ""
    private var addOutboundLabel: String = ""
    private var addCartLabel: String = ""

    fun bindLabel(error: String, outbound: String, cart: String) {
        errorLabel = error
        addOutboundLabel = outbound
        addCartLabel = cart
    }

    fun onEvent(event: PartDetailUiEvent) {
        when (event) {
            is PartDetailUiEvent.Initialize -> {
                _uiState.update {
                    it.copy(
                        part = event.part,
                        quantity = 1,
                        isUpdating = false,
                        isOutboundSuccess = false,
                        isCartSuccess = false
                    )
                }
            }
            is PartDetailUiEvent.IncreaseQuantity -> {
                val currentQuantity = _uiState.value.quantity
                _uiState.update { it.copy(quantity = currentQuantity + 1) }
            }
            is PartDetailUiEvent.DecreaseQuantity -> {
                val currentQuantity = _uiState.value.quantity
                _uiState.update { it.copy(quantity = maxOf(1L, currentQuantity - 1)) }
            }
            is PartDetailUiEvent.SetQuantity -> {
                if (event.quantity > 0) {
                    _uiState.update { it.copy(quantity = event.quantity) }
                }
            }
            is PartDetailUiEvent.AddToOutbound -> {
                val part = _uiState.value.part
                val quantity = _uiState.value.quantity
                if (part != null) {
                    addToOutbound(part.partId, quantity)
                }
            }
            is PartDetailUiEvent.AddToCart -> {
                val part = _uiState.value.part
                val quantity = _uiState.value.quantity
                if (part != null) {
                    addToCart(part.partId, quantity)
                }
            }
            is PartDetailUiEvent.Dismiss -> {
                _uiState.update {
                    it.copy(
                        part = null,
                        quantity = 1
                    )
                }
            }
        }
    }

    private fun addToOutbound(partId: Long, quantity: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, updateError = null) }

            addOutboundUseCase(partId, quantity)
                .onSuccess {
                    messageHandler.showMessage(message= addOutboundLabel, isError = false)
                    _uiState.update { it.copy(isUpdating = false, isOutboundSuccess = true) }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(isUpdating = false, updateError = error)
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun addToCart(partId: Long, quantity: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, updateError = null) }

            addCartUseCase(partId, quantity)
                .onSuccess {
                    messageHandler.showMessage(message= addCartLabel, isError = false)
                    _uiState.update { it.copy(isUpdating = false, isCartSuccess = true) }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(isUpdating = false, updateError = error)
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    fun clearStatus() {
        _uiState.update { it.copy(isOutboundSuccess = false, isCartSuccess = false, updateError = null) }
    }
}