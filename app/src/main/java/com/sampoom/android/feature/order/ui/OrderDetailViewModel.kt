package com.sampoom.android.feature.order.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.order.domain.usecase.CancelOrderUseCase
import com.sampoom.android.feature.order.domain.usecase.GetOrderDetailUseCase
import com.sampoom.android.feature.order.domain.usecase.ReceiveOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val getOrderDetailUseCase: GetOrderDetailUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val receiveOrderUseCase: ReceiveOrderUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        private const val TAG = "OrderDetailViewModel"
    }

    private val _uiState = MutableStateFlow(OrderDetailUiState())
    val uiState: StateFlow<OrderDetailUiState> = _uiState

    // Navigation 인자 로드
    private val agencyId: Long = savedStateHandle.get<Long>("agencyId") ?: 0L
    private val navOrderId: Long = savedStateHandle.get<Long>("orderId") ?: 0L

    private var apiOrderId: Long? = null

    fun setOrderIdFromApi(orderId: Long) {
        apiOrderId = orderId
    }

    private fun getOrderId(): Long {
        return apiOrderId ?: navOrderId
    }

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        if (getOrderId() > 0L) loadOrderDetail(getOrderId())
        else _uiState.update { it.copy(orderDetailError = errorLabel) }
    }

    fun onEvent(event: OrderDetailUiEvent) {
        when (event) {
            is OrderDetailUiEvent.LoadOrder -> loadOrderDetail(getOrderId())
            is OrderDetailUiEvent.RetryOrder -> loadOrderDetail(getOrderId())
            is OrderDetailUiEvent.CancelOrder -> cancelOrder(getOrderId())
            is OrderDetailUiEvent.ReceiveOrder -> receiveOrder(getOrderId())
            is OrderDetailUiEvent.ClearError -> _uiState.update { it.copy(isProcessingError = null) }
        }
    }

    private fun loadOrderDetail(orderId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(orderDetailLoading = true, orderDetailError = null) }

            try {
                val orderList = getOrderDetailUseCase(orderId)
                _uiState.update {
                    it.copy(
                        orderDetail = orderList.items,
                        orderDetailLoading = false,
                        orderDetailError = null
                    )
                }
            } catch (ce : CancellationException) {
                throw ce
            } catch (throwable : Throwable) {
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

    private fun cancelOrder(orderId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, isProcessingError = null) }

            cancelOrderUseCase(orderId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            isProcessingCancelSuccess = true,
                            isProcessingError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            isProcessingError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    private fun receiveOrder(orderId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, isProcessingError = null) }

            receiveOrderUseCase(orderId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            isProcessingReceiveSuccess = true,
                            isProcessingError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            isProcessing = false,
                            isProcessingError = backendMessage ?: (throwable.message ?: errorLabel)
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isProcessingCancelSuccess = false, isProcessingReceiveSuccess = false) }
    }
}