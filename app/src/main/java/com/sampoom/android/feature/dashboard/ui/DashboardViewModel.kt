package com.sampoom.android.feature.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.auth.domain.model.User
import com.sampoom.android.feature.auth.domain.usecase.GetStoredUserUseCase
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.usecase.GetOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val getOrderListUseCase: GetOrderUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase
): ViewModel() {

    private companion object {
        private const val TAG = "DashboardViewModel"
    }

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState : StateFlow<DashboardUiState> = _uiState

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    val orderListPaged: Flow<PagingData<Order>> = getOrderListUseCase()
        .cachedIn(viewModelScope)

    private var errorLabel: String = ""
    private var loadJob: Job? = null

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        viewModelScope.launch {
            _user.value = getStoredUserUseCase()
        }
    }

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.LoadDashboard -> {}//loadOrderList()
            is DashboardUiEvent.RetryDashboard -> {}//loadOrderList()
        }
    }

//    private fun loadOrderList() {
//        if (loadJob?.isActive == true) return
//        loadJob = viewModelScope.launch {
//            _uiState.update { it.copy(dashboardLoading = true, dashboardError = null) }
//
//            getOrderListUseCase()
//                .onSuccess { orderList ->
//                    _uiState.update {
//                        it.copy(
//                            orderList = orderList.items.take(5),
//                            dashboardLoading = false,
//                            dashboardError = null
//                        )
//                    }
//                }
//                .onFailure { throwable ->
//                    val backendMessage = throwable.serverMessageOrNull()
//                    _uiState.update {
//                        it.copy(
//                            dashboardLoading = false,
//                            dashboardError = backendMessage ?: (throwable.message ?: errorLabel)
//                        )
//                    }
//                }
//            Log.d(TAG, "submit: ${_uiState.value}")
//        }
//    }
}