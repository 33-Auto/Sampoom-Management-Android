package com.sampoom.android.feature.order.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.order.domain.usecase.GetOrderDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        private const val TAG = "OrderDetailViewModel"
    }

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState

    // Navigation 인자 로드
    private val agencyId: Long = savedStateHandle.get<Long>("agencyId") ?: 0L
    private val orderId: Long = savedStateHandle.get<Long>("orderId") ?: 0L

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        if (orderId > 0L) loadOrderDetail(orderId)
        else _uiState.update { it.copy(orderDetailError = errorLabel) }
    }

    fun onEvent(event: OrderDetailUiEvent) {
        when (event) {
            is OrderDetailUiEvent.LoadOrder -> loadOrderDetail(orderId)
            is OrderDetailUiEvent.RetryOrder -> loadOrderDetail(orderId)
        }
    }

    private fun loadOrderDetail(orderId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(orderDetailLoading = true, orderDetailError = null) }

            runCatching { getOrderDetailUseCase(orderId)  }
                .onSuccess { orderList ->
                    _uiState.update {
                        it.copy(
                            orderDetail = orderList.items,
                            orderDetailLoading = false,
                            orderDetailError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            orderDetailLoading = false,
                            orderDetailError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }
}