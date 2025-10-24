package com.sampoom.android.feature.order.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.ButtonVariant
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.feature.order.domain.model.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()
    var showCancelOrderDialog by remember { mutableStateOf(false) }
    var showReceiveOrderDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // 성공 시 Toast 표시 후 다이얼로그 닫기
    LaunchedEffect(uiState.isProcessingCancelSuccess) {
        if (uiState.isProcessingCancelSuccess) {
            Toast.makeText(context, context.getString(R.string.order_detail_toast_order_cancel), Toast.LENGTH_SHORT).show()
            viewModel.clearSuccess()
            viewModel.onEvent(OrderDetailUiEvent.LoadOrder)
        }
    }

    // 성공 시 Toast 표시 후 다이얼로그 닫기
    LaunchedEffect(uiState.isProcessingReceiveSuccess) {
        if (uiState.isProcessingReceiveSuccess) {
            Toast.makeText(context, context.getString(R.string.order_detail_toast_order_receive), Toast.LENGTH_SHORT).show()
            viewModel.clearSuccess()
            viewModel.onEvent(OrderDetailUiEvent.LoadOrder)
        }
    }

    // 실패 시 Toast 표시
    LaunchedEffect(uiState.isProcessingError) {
        uiState.isProcessingError?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.onEvent(OrderDetailUiEvent.ClearError)
        }
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { viewModel.onEvent(OrderDetailUiEvent.LoadOrder) },
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.orderDetailLoading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullRefreshState
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.order_detail_title)) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = stringResource(R.string.nav_back)
                            )
                        }
                    }
                )
            },
            bottomBar = {
                val orderStatus = uiState.orderDetail.firstOrNull()?.status
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CommonButton(
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.Error,
                        enabled = orderStatus != null &&
                                !uiState.isProcessing &&
                                orderStatus == OrderStatus.PENDING,
                        onClick = { showCancelOrderDialog = true }
                    ) {
                        Text(stringResource(R.string.order_detail_order_cancel))
                    }
                    Spacer(Modifier.width(16.dp))
                    CommonButton(
                        modifier = Modifier.weight(1f),
                        enabled = orderStatus != null &&
                                !uiState.isProcessing &&
                                orderStatus == OrderStatus.PENDING,
                        onClick = { showReceiveOrderDialog = true }
                    ) {
                        Text(stringResource(R.string.order_detail_order_receive))
                    }
                }
            }
        ) { innerPadding ->
            when {
                uiState.orderDetailLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.orderDetailError != null -> {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(OrderDetailUiEvent.RetryOrder) },
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }

                uiState.orderDetail.isEmpty() -> {
                    EmptyContent(
                        message = stringResource(R.string.order_empty_list),
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                    )
                }

                else -> {
                    OrderDetailContent(
                        order = uiState.orderDetail,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    if (showCancelOrderDialog) {
        AlertDialog(
            onDismissRequest = { showCancelOrderDialog = false },
            text = { Text(stringResource(R.string.order_detail_dialog_order_cancel)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelOrderDialog = false
                        viewModel.onEvent(OrderDetailUiEvent.CancelOrder)
                    }
                ) {
                    Text(stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelOrderDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

    if (showReceiveOrderDialog) {
        AlertDialog(
            onDismissRequest = { showReceiveOrderDialog = false },
            text = { Text(stringResource(R.string.order_detail_dialog_order_receive)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showReceiveOrderDialog = false
                        viewModel.onEvent(OrderDetailUiEvent.ReceiveOrder)
                    }
                ) {
                    Text(stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showReceiveOrderDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }
}