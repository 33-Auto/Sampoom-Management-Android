package com.sampoom.android.feature.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sampoom.android.core.network.serverMessageOrNull
import com.sampoom.android.core.util.GlobalMessageHandler
import com.sampoom.android.feature.dashboard.domain.usecase.GetDashboardUseCase
import com.sampoom.android.feature.dashboard.domain.usecase.WeeklySummaryUseCase
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.usecase.GetOrderUseCase
import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.domain.usecase.GetEmployeeCountUseCase
import com.sampoom.android.feature.user.domain.usecase.GetEmployeeUseCase
import com.sampoom.android.feature.user.domain.usecase.GetStoredUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val messageHandler: GlobalMessageHandler,
    private val getOrderListUseCase: GetOrderUseCase,
    private val getStoredUserUseCase: GetStoredUserUseCase,
    private val getDashboardUseCase: GetDashboardUseCase,
    private val getWeeklySummaryUseCase: WeeklySummaryUseCase,
    private val getEmployeeCountUseCase: GetEmployeeCountUseCase
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

    fun bindLabel(error: String) {
        errorLabel = error
    }

    init {
        loadDashboard()
        loadWeeklySummary()
        loadEmployeeCount()
        viewModelScope.launch {
            _user.value = getStoredUserUseCase()
        }
    }

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.LoadDashboard -> {
                loadDashboard()
                loadWeeklySummary()
                loadEmployeeCount()
            }
            is DashboardUiEvent.RetryDashboard -> {
                loadDashboard()
                loadWeeklySummary()
                loadEmployeeCount()
            }
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.update { it.copy(dashboardLoading = true, dashboardError = null) }

            getDashboardUseCase()
                .onSuccess { dashboard ->
                    _uiState.update {
                        it.copy(
                            dashboard = dashboard,
                            dashboardLoading = false,
                            dashboardError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            dashboardLoading = false,
                            dashboardError = error
                        )
                    }
                }
        }
    }

    private fun loadWeeklySummary() {
        viewModelScope.launch {
            _uiState.update { it.copy(weeklySummaryLoading = true, weeklySummaryError = null) }

            getWeeklySummaryUseCase()
                .onSuccess { weeklySummary ->
                    _uiState.update {
                        it.copy(
                            weeklySummary = weeklySummary,
                            weeklySummaryLoading = false,
                            weeklySummaryError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            weeklySummaryLoading = false,
                            weeklySummaryError = error
                        )
                    }
                }
        }
    }

    private fun loadEmployeeCount() {
        viewModelScope.launch {
            _uiState.update { it.copy(employeeCountLoading = true, employeeCountError = null) }

            getEmployeeCountUseCase()
                .onSuccess { count ->
                    _uiState.update {
                        it.copy(
                            employeeCount = count,
                            employeeCountLoading = false,
                            employeeCountError = null
                        )
                    }
                }
                .onFailure { throwable ->
                    val backendMessage = throwable.serverMessageOrNull()
                    val error = backendMessage ?: (throwable.message ?: errorLabel)
                    messageHandler.showMessage(message = error, isError = true)

                    _uiState.update {
                        it.copy(
                            weeklySummaryLoading = false,
                            weeklySummaryError = error
                        )
                    }
                }
        }
    }

    fun refreshUser() {
        viewModelScope.launch {
            _user.value = getStoredUserUseCase()
        }
    }
}