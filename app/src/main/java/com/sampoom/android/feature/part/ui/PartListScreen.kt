package com.sampoom.android.feature.part.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.feature.outbound.ui.OutboundListUiEvent
import com.sampoom.android.feature.part.domain.model.Part

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartListScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: PartListViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()

    // ModalBottomSheet 상태 관리
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = uiState.partListLoading,
        onRefresh = { viewModel.onEvent(PartListUiEvent.LoadPartList) },
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.partListLoading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullRefreshState
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.part_title)) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = stringResource(R.string.nav_back)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            when {
                uiState.partListLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.partListError != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorContent(
                            onRetry = { viewModel.onEvent(PartListUiEvent.RetryPartList) },
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                uiState.partList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyContent(
                            message = stringResource(R.string.part_empty_part),
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.partList) { part ->
                            PartListItemCard(
                                part = part,
                                onClick = {
                                    viewModel.onEvent(PartListUiEvent.ShowBottomSheet(part))
                                    showBottomSheet = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        uiState.selectedPart?.let { selectedPart ->
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                    viewModel.onEvent(PartListUiEvent.DismissBottomSheet)
                },
                sheetState = sheetState
            ) {
                PartDetailBottomSheet(
                    part = selectedPart,
                    onDismiss = {
                        showBottomSheet = false
                        viewModel.onEvent(PartListUiEvent.DismissBottomSheet)
                    }
                )
            }
        }
    }
}

@Composable
private fun PartListItemCard(
    part: Part,
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1F)
            ) {
                Text(
                    text = part.name,
                    color = textColor(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = part.code,
                    color = textSecondaryColor(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Light
                )
            }

            Text(
                text = part.quantity.toString(),
                color = textColor(),
                style = MaterialTheme.typography.titleMedium
            )

            Icon(
                painterResource(R.drawable.chevron_right),
                contentDescription = stringResource(R.string.common_detail),
                tint = textSecondaryColor()
            )
        }
    }
}