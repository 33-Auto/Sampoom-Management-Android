package com.sampoom.android.feature.part.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.outbound.domain.usecase.AddOutboundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PartDetailViewModel @Inject constructor(
    private val addOutboundUseCase: AddOutboundUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PartDetailUiState())
    val uiState: StateFlow<PartDetailUiState> = _uiState

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    fun onEvent(event: PartDetailUiEvent) {
        when (event) {
            is PartDetailUiEvent.Initialize -> {
                _uiState.update {
                    it.copy(
                        part = event.part,
                        quantity = 1,
                        isUpdating = false,
                        updateError = null,
                        isSuccess = false
                    )
                }
            }
            is PartDetailUiEvent.IncreaseQuantity -> {
                val currentQuantity = _uiState.value.quantity
                _uiState.update { it.copy(quantity = currentQuantity + 1) }
            }
            is PartDetailUiEvent.DecreaseQuantity -> {
                val currentQuantity = _uiState.value.quantity
                _uiState.update { it.copy(quantity = currentQuantity - 1) }
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
            is PartDetailUiEvent.ClearError -> _uiState.update { it.copy(updateError = null) }
            is PartDetailUiEvent.Dismiss -> {
                _uiState.update {
                    it.copy(
                        part = null,
                        quantity = 1,
                        updateError = null
                    )
                }
            }
        }
    }

    private fun addToOutbound(partId: Long, quantity: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, updateError = null) }

            runCatching { addOutboundUseCase(partId, quantity) }
                .onSuccess {
                    _uiState.update { it.copy(isUpdating = false, isSuccess = true) }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            updateError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d("OutboundDetailViewModel", "submit: ${_uiState.value}")
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}