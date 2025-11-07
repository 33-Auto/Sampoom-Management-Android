package com.sampoom.android.feature.outbound.ui

import android.widget.Toast
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.sampoom.android.core.ui.component.ButtonSize
import com.sampoom.android.core.ui.component.ButtonVariant
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.theme.FailRed
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.core.util.formatWon
import com.sampoom.android.feature.cart.domain.model.subtotal
import com.sampoom.android.feature.outbound.domain.model.OutboundPart
import com.sampoom.android.feature.outbound.domain.model.subtotal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutboundListScreen(
    paddingValues: PaddingValues,
    viewModel: OutboundListViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)
    val successLabel = stringResource(R.string.outbound_toast_order_text)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()
    var showEmptyOutboundDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.clearSuccess()
    }

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel, successLabel)
        viewModel.onEvent(OutboundListUiEvent.LoadOutboundList)
    }

    LaunchedEffect(uiState.isOrderSuccess) {
        if (uiState.isOrderSuccess) {
            viewModel.clearSuccess()
        }
    }
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { viewModel.onEvent(OutboundListUiEvent.LoadOutboundList) },
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.outboundLoading,
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
                    text = stringResource(R.string.outbound_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor()
                )

                when {
                    uiState.outboundLoading -> {}
                    uiState.outboundError != null -> {}
                    uiState.outboundList.isEmpty() -> {}
                    else -> {
                        TextButton(
                            onClick = { showEmptyOutboundDialog = true }
                        ) {
                            Text(
                                text = stringResource(R.string.outbound_empty_list),
                                style = MaterialTheme.typography.titleMedium,
                                color = FailRed
                            )
                        }
                    }
                }
            }

            when {
                uiState.outboundLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.outboundError != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorContent(
                            onRetry = { viewModel.onEvent(OutboundListUiEvent.RetryOutboundList) },
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                uiState.outboundList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyContent(
                            message = stringResource(R.string.outbound_empty_outbound),
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            uiState.outboundList.forEach { category ->
                                category.groups.forEach { group ->
                                    item {
                                        OutboundSection(
                                            categoryName = category.categoryName,
                                            groupName = group.groupName,
                                            parts = group.parts,
                                            isUpdating = uiState.isUpdating,
                                            isDeleting = uiState.isDeleting,
                                            onEvent = { viewModel.onEvent(it) }
                                        )
                                    }
                                }
                            }
                            item { Spacer(Modifier.height(100.dp)) }
                        }

                        CommonButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .padding(end = 72.dp),
                            variant = ButtonVariant.Error,
                            size = ButtonSize.Large,
                            onClick = { showConfirmDialog = true }
                        ) { Text("${formatWon(uiState.totalCost)} ${stringResource(R.string.outbound_order_parts)}") }
                    }
                }
            }
        }
    }

    if (showEmptyOutboundDialog) {
        AlertDialog(
            onDismissRequest = { showEmptyOutboundDialog = false },
            text = { Text(stringResource(R.string.outbound_dialog_empty_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEmptyOutboundDialog = false
                        viewModel.onEvent(OutboundListUiEvent.DeleteAllOutbound)
                    }
                ) {
                    Text(stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEmptyOutboundDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            text = { Text(stringResource(R.string.outbound_dialog_order_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.onEvent(OutboundListUiEvent.ProcessOutbound)
                    }
                ) {
                    Text(stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }
}

@Composable
private fun OutboundSection(
    categoryName: String,
    groupName: String,
    parts: List<OutboundPart>,
    isUpdating: Boolean,
    isDeleting: Boolean,
    onEvent: (OutboundListUiEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$categoryName > $groupName",
            style = MaterialTheme.typography.titleMedium,
            color = textColor()
        )

        parts.forEach { part ->
            OutboundPartItem(
                part = part,
                isUpdating = isUpdating,
                isDeleting = isDeleting,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun OutboundPartItem(
    part: OutboundPart,
    isUpdating: Boolean,
    isDeleting: Boolean,
    onEvent: (OutboundListUiEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundCardColor())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1F)
                ) {
                    Text(
                        text = part.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = part.code,
                        style = MaterialTheme.typography.bodySmall,
                        color = textSecondaryColor(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                IconButton(
                    onClick = {
                        onEvent(OutboundListUiEvent.DeleteOutbound(part.outboundId))
                    },
                    enabled = !isDeleting
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete),
                        contentDescription = stringResource(R.string.common_delete),
                        tint = FailRed
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = stringResource(R.string.part_title_quantity), Modifier.weight(1F))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CommonButton(
                        variant = ButtonVariant.Neutral,
                        size = ButtonSize.Large,
                        onClick = {
                            if (part.quantity > 1) onEvent(
                                OutboundListUiEvent.UpdateQuantity(
                                    part.outboundId,
                                    part.quantity - 1
                                )
                            )
                        },
                        enabled = !isUpdating && part.quantity > 1
                    ) {
                        Text(
                            text = stringResource(R.string.part_minus),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    Text(
                        text = part.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    CommonButton(
                        variant = ButtonVariant.Neutral,
                        size = ButtonSize.Large,
                        onClick = {
                            onEvent(
                                OutboundListUiEvent.UpdateQuantity(
                                    part.outboundId,
                                    part.quantity + 1
                                )
                            )
                        },
                        enabled = !isUpdating
                    ) {
                        Text(
                            text = stringResource(R.string.part_plus),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
            val subtotal = part.subtotal

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = formatWon(subtotal),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}