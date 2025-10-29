package com.sampoom.android.feature.outbound.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.outbound.domain.usecase.DeleteAllOutboundUseCase
import com.sampoom.android.feature.outbound.domain.usecase.DeleteOutboundUseCase
import com.sampoom.android.feature.outbound.domain.usecase.GetOutboundUseCase
import com.sampoom.android.feature.outbound.domain.usecase.ProcessOutboundUseCase
import com.sampoom.android.feature.outbound.domain.usecase.UpdateOutboundQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OutboundListViewModel @Inject constructor(
    private val getOutboundUseCase: GetOutboundUseCase,
    private val processOutboundUseCase: ProcessOutboundUseCase,
    private val updateOutboundQuantityUseCase: UpdateOutboundQuantityUseCase,
    private val deleteOutboundUseCase: DeleteOutboundUseCase,
    private val deleteAllOutboundUseCase: DeleteAllOutboundUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "OutboundListViewModel"
    }

    private val _uiState = MutableStateFlow(OutboundListUiState())
    val uiState: StateFlow<OutboundListUiState> = _uiState

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        loadOutboundList()
    }

    fun onEvent(event: OutboundListUiEvent) {
        when (event) {
            is OutboundListUiEvent.LoadOutboundList -> loadOutboundList()
            is OutboundListUiEvent.RetryOutboundList -> loadOutboundList()
            is OutboundListUiEvent.ProcessOutbound -> processOutBound()
            is OutboundListUiEvent.UpdateQuantity -> updateQuantity(event.outboundId, event.quantity)
            is OutboundListUiEvent.DeleteOutbound -> deleteOutbound(event.outboundId)
            is OutboundListUiEvent.DeleteAllOutbound -> deleteAllOutbound()
            is OutboundListUiEvent.ClearUpdateError -> _uiState.update { it.copy(updateError = null) }
            is OutboundListUiEvent.ClearDeleteError -> _uiState.update { it.copy(deleteError = null) }
        }
    }

    private fun loadOutboundList() {
        viewModelScope.launch {
            _uiState.update { it.copy(outboundLoading = true, outboundError = null) }

            getOutboundUseCase()
                .onSuccess { outboundList ->
                    _uiState.update {
                        it.copy(
                            outboundList = outboundList.items,
                            outboundLoading = false,
                            outboundError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            outboundLoading = false,
                            outboundError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun processOutBound() {
        viewModelScope.launch {
            _uiState.update { it.copy(outboundLoading = true, outboundError = null) }

            processOutboundUseCase()
                .onSuccess {
                    _uiState.update { it.copy(outboundLoading = false, isOrderSuccess = true) }
                    loadOutboundList()
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            outboundLoading = false,
                            outboundError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun updateQuantity(outboundId: Long, newQuantity: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isUpdating = true, updateError = null) }

            updateOutboundQuantityUseCase(outboundId, newQuantity)
                .onSuccess {
                    _uiState.update { it.copy(isUpdating = false) }
                    updateLocalQuantity(outboundId, newQuantity)
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
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun updateLocalQuantity(outboundId: Long, newQuantity: Long) {
        _uiState.update { currentState ->
            val updatedList = currentState.outboundList.map { category ->
                category.copy(
                    groups = category.groups.map { group ->
                        group.copy(
                            parts = group.parts.map { part ->
                                if (part.outboundId == outboundId) {
                                    part.copy(quantity = newQuantity)
                                } else {
                                    part
                                }
                            }
                        )
                    }
                )
            }
            currentState.copy(outboundList = updatedList)
        }
    }

    private fun deleteOutbound(outboundId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, deleteError = null) }

            deleteOutboundUseCase(outboundId)
                .onSuccess {
                    _uiState.update { it.copy(isDeleting = false) }
                    removeFromLocalList(outboundId)
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            deleteError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun deleteAllOutbound() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true, deleteError = null) }

            deleteAllOutboundUseCase()
                .onSuccess {
                    _uiState.update { it.copy(isDeleting = false) }
                    removeAllFromLocalList()
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            deleteError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun removeFromLocalList(outboundId: Long) {
        _uiState.update { currentState ->
            val updatedList = currentState.outboundList.map { category ->
                category.copy(
                    groups = category.groups.map { group ->
                        group.copy(
                            parts = group.parts.filter { part ->
                                part.outboundId != outboundId
                            }
                        )
                    }.filter { group ->
                        group.parts.isNotEmpty()
                    }
                )
            }.filter { category ->
                category.groups.isNotEmpty()
            }
            currentState.copy(outboundList = updatedList)
        }
    }

    private fun removeAllFromLocalList() {
        _uiState.update { currentState ->
            currentState.copy(outboundList = emptyList())
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isOrderSuccess = false) }
    }
}