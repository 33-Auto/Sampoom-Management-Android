package com.sampoom.android.feature.user.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
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
import com.sampoom.android.core.util.formatDate
import com.sampoom.android.core.util.positionToKorean
import com.sampoom.android.feature.user.domain.model.Employee
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: EmployeeListViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val errorLabel = stringResource(R.string.common_error)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val employeeListPaged = viewModel.employeeListPaged.collectAsLazyPagingItems()
    val pullRefreshState = rememberPullToRefreshState()
    val listState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    val sheetState = rememberModalBottomSheetState(true)
    val selectedEmployee = uiState.selectedEmployee

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
    }

    LaunchedEffect(Unit) {
        employeeListPaged.refresh()
    }

    LaunchedEffect(selectedEmployee) {
        if (selectedEmployee != null && !sheetState.isVisible) {
            sheetState.show()
        } else if (selectedEmployee == null && sheetState.isVisible) {
            sheetState.hide()
        }
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { employeeListPaged.refresh() },
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.employeeLoading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullRefreshState
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.employee_title)) },
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
            when (employeeListPaged.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        ErrorContent(
                            onRetry = { viewModel.onEvent(EmployeeListUiEvent.RetryEmployeeList) },
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                else -> {
                    if (employeeListPaged.loadState.refresh !is LoadState.Loading && employeeListPaged.itemCount == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            EmptyContent(
                                message = stringResource(R.string.employee_empty_employee),
                                modifier = Modifier.height(200.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(
                                count = employeeListPaged.itemCount,
                                key = employeeListPaged.itemKey { it.userId }
                            ) { index ->
                                val employee = employeeListPaged[index]
                                if (employee != null) {
                                    EmployeeListItemCard(
                                        employee = employee,
                                        onDeleteClick = {

                                        },
                                        onEditClick = {
                                            viewModel.onEvent(EmployeeListUiEvent.ShowBottomSheet(employee))
                                        }
                                    )
                                }
                            }

                            // 로딩 상태 처리
                            item {
                                when (employeeListPaged.loadState.append) {
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

    if (selectedEmployee != null) {
        uiState.selectedEmployee?.let { selectedEmployee ->
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        viewModel.onEvent(EmployeeListUiEvent.DismissBottomSheet)
                        sheetState.hide()
                    }
                },
                sheetState = sheetState
            ) {
                EditEmployeeBottomSheet(
                    employee = selectedEmployee,
                    onDismiss = {
                        coroutineScope.launch {
                            viewModel.onEvent(EmployeeListUiEvent.DismissBottomSheet)
                            sheetState.hide()
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun EmployeeListItemCard(
    employee: Employee,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
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
            Text(
                text = employee.userName,
                color = textColor(),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = positionToKorean(employee.position),
                color = textSecondaryColor(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.employee_email),
                    color = textSecondaryColor(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = employee.email,
                    color = textSecondaryColor(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.employee_startedAt),
                    color = textSecondaryColor(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = formatDate(employee.startedAt ?: stringResource(R.string.common_slash)),
                    color = textSecondaryColor(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CommonButton(
                    modifier = Modifier.weight(1F),
                    variant = ButtonVariant.Error,
                    size = ButtonSize.Large,
                    onClick = { onDeleteClick() }
                ) {
                    Text(stringResource(R.string.employee_delete))
                }
                Spacer(Modifier.width(8.dp))
                CommonButton(
                    modifier = Modifier.weight(1F),
                    variant = ButtonVariant.Primary,
                    size = ButtonSize.Large,
                    onClick = { onEditClick() }
                ) {
                    Text(stringResource(R.string.employee_edit))
                }
            }
        }
    }
}