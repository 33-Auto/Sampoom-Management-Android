package com.sampoom.android.feature.order.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.feature.order.domain.usecase.GetOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val getOrderListUseCase: GetOrderUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "OrderListViewModel"
    }

    private val _uiState = MutableStateFlow(OrderListUiState())
    val uiState: StateFlow<OrderListUiState> = _uiState

    private var errorLabel: String = ""
    private var loadJob: Job? = null

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        loadOrderList()
    }

    fun onEvent(event: OrderListUiEvent) {
        when (event) {
            is OrderListUiEvent.LoadOrderList -> loadOrderList()
            is OrderListUiEvent.RetryOrderList -> loadOrderList()
        }
    }

    private fun loadOrderList() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(orderLoading = true, orderError = null) }

            getOrderListUseCase()
                .onSuccess { orderList ->
                    _uiState.update {
                        it.copy(
                            orderList = orderList.items,
                            orderLoading = false,
                            orderError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    _uiState.update {
                        it.copy(
                            orderLoading = false,
                            orderError = backendMessage ?: (throwable.message ?: errorLabel )
                        )
                    }
                }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }
}