package com.sampoom.android.feature.order.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.component.StatusChip
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.core.util.buildOrderTitle
import com.sampoom.android.core.util.formatDate
import com.sampoom.android.feature.order.domain.model.Order

@Composable
fun OrderListScreen(
    onNavigateOrderDetail: (Order) -> Unit,
    viewModel: OrderListViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
        viewModel.onEvent(OrderListUiEvent.LoadOrderList)
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                text = stringResource(R.string.order_title),
                style = MaterialTheme.typography.titleLarge,
                color = textColor()
            )
        }

        when {
            uiState.orderLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.orderError != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(OrderListUiEvent.RetryOrderList) },
                        modifier = Modifier.height(200.dp)
                    )
                }
            }

            uiState.orderList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyContent(
                        message = stringResource(R.string.order_empty_list),
                        modifier = Modifier.height(200.dp)
                    )
                }
            }

            else -> {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.orderList.forEach { order ->
                        item {
                            OrderItem(
                                order = order,
                                onClick = { onNavigateOrderDetail(order) }
                            )
                        }
                    }
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Composable
private fun OrderItem(
    order: Order,
    onClick: () -> Unit
) {
    Card(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundCardColor())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = buildOrderTitle(order),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = order.agencyName ?: stringResource(R.string.common_slash),
                    style = MaterialTheme.typography.labelMedium,
                    color = textSecondaryColor()
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = order.createdAt?.let { formatDate(it) } ?: stringResource(R.string.common_slash),
                    style = MaterialTheme.typography.labelMedium,
                    color = textSecondaryColor()
                )
                Spacer(Modifier.height(6.dp))
                StatusChip(status = order.status)
            }
        }
    }
}