package com.sampoom.android.feature.order.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.theme.FailRed
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.feature.order.domain.model.Order

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderListScreen(
    paddingValues: PaddingValues,
    onNavigateOrderDetail: (Order) -> Unit,
    viewModel: OrderListViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val orderListPaged = viewModel.orderListPaged.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullToRefreshState()
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
    }

    LaunchedEffect(Unit) {
        orderListPaged.refresh()
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { orderListPaged.refresh() },
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.orderLoading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullRefreshState
            )
        }
    ) {
        Column(Modifier.fillMaxSize().padding(paddingValues)) {
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

            when (orderListPaged.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorContent(
                            onRetry = { orderListPaged.retry() },
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                else -> {
                    // 빈 상태 처리
                    if (orderListPaged.loadState.refresh !is LoadState.Loading && orderListPaged.itemCount == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyContent(
                                message = stringResource(R.string.order_empty_list),
                                modifier = Modifier.height(200.dp)
                            )
                        }
                    }
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = orderListPaged.itemCount,
                            key = orderListPaged.itemKey { it.orderId }
                        ) { index ->
                            val order = orderListPaged[index]
                            if (order != null) {
                                OrderItem(
                                    order = order,
                                    onClick = { onNavigateOrderDetail(order) }
                                )
                            }
                        }

                        // 로딩 상태 처리
                        item {
                            when (orderListPaged.loadState.append) {
                                is LoadState.Loading -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                                is LoadState.Error -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.common_error),
                                            color = FailRed
                                        )
                                    }
                                }
                                else -> {}
                            }
                        }

                        item { Spacer(Modifier.height(100.dp)) }
                    }
                }
            }
        }
    }
}