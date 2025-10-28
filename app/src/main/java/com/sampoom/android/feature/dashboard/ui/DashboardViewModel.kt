package com.sampoom.android.feature.dashboard.ui

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
class DashboardViewModel @Inject constructor(
    private val getOrderListUseCase: GetOrderUseCase
): ViewModel() {

    private companion object {
        private const val TAG = "DashboardViewModel"
    }

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState : StateFlow<DashboardUiState> = _uiState

    private var errorLabel: String = ""
    private var loadJob: Job? = null

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        loadOrderList()
    }

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.LoadDashboard -> loadOrderList()
            is DashboardUiEvent.RetryDashboard -> loadOrderList()
        }
    }

    private fun loadOrderList() {
        if (loadJob?.isActive == true) return
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(dashboardLoading = true, dashboardError = null) }

            try {
                val orderList = withContext(Dispatchers.IO) { getOrderListUseCase() }
                _uiState.update {
                    it.copy(
                        orderList = orderList.items.take(5),
                        dashboardLoading = false,
                        dashboardError = null
                    )
                }
            } catch (ce: CancellationException) {
                throw ce
            } catch (throwable: Throwable) {
                val backendMessage = throwable.serverMessageOrNull()
                _uiState.update {
                    it.copy(
                        dashboardLoading = false,
                        dashboardError = backendMessage ?: (throwable.message ?: errorLabel )
                    )
                }
            }
            Log.d(TAG, "submit: ${_uiState.value}")
        }
    }
}