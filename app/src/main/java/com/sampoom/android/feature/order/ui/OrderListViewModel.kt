package com.sampoom.android.feature.order.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.usecase.GetOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val getOrderListUseCase: GetOrderUseCase
) : ViewModel() {

    private companion object {
        private const val TAG = "OrderListViewModel"
    }

    private val _uiState = MutableStateFlow(OrderListUiState())
    val uiState: StateFlow<OrderListUiState> = _uiState

    private var errorLabel: String = ""

    fun bindLabel(error: String) {
        errorLabel = error
    }

    val orderListPaged: Flow<PagingData<Order>> = getOrderListUseCase()
        .cachedIn(viewModelScope)

    fun onEvent(event: OrderListUiEvent) {
        when (event) {
            is OrderListUiEvent.LoadOrderList -> {}//loadOrderList()
            is OrderListUiEvent.RetryOrderList -> {}//loadOrderList()
        }
    }
}