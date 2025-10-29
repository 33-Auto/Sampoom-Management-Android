package com.sampoom.android.feature.dashboard.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.component.OrderItem
import com.sampoom.android.core.ui.theme.Main500
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.feature.order.domain.model.Order

@Composable
fun DashboardScreen(
    paddingValues: PaddingValues,
    onNavigateOrderDetail: (Order) -> Unit,
    onNavigationOrder: () -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()
    val isManager = true // TODO: Role 검증

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = { viewModel.onEvent(DashboardUiEvent.LoadDashboard) },
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.dashboardLoading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullRefreshState
            )
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .height(24.dp),
                    painter = painterResource(id = R.drawable.oneline_logo),
                    contentDescription = stringResource(R.string.app_name)
                )

                Row {
                    if (isManager) {
                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.employee),
                                contentDescription = stringResource(R.string.nav_employee)
                            )
                        }
                    }

                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.settings),
                            contentDescription = stringResource(R.string.nav_setting)
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Button(onClick = { onLogoutClick() }) {
                        Text("로그아웃")
                    }
                }

                item { TitleSection() }

                item { ButtonSection(isManager) }

                item {
                    OrderListSection(
                        viewModel = viewModel,
                        uiState = uiState,
                        onNavigateOrderDetail = { order ->
                            onNavigateOrderDetail(order)
                        },
                        onNavigationOrder = {
                            onNavigationOrder()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TitleSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "가산디지털단지점", // TODO: Agency Id 받아오기
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = textColor()
        )

        val welcomeText = buildAnnotatedString {
            append(stringResource(R.string.dashboard_title_hello))

            pushStringAnnotation(tag = "NAME", annotation = "name")
            withStyle(style = SpanStyle(color = Main500)) {
                append("홍길동")   // TODO : 이름 받아오기
            }
            append(stringResource(R.string.dashboard_title_hello_sir))
            pop()
        }

        Text(
            text = welcomeText,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = textColor()
        )

        Text(
            text = stringResource(R.string.dashboard_title_description),
            style = MaterialTheme.typography.bodyLarge,
            color = textColor()
        )
    }
}

@Composable
fun ButtonSection(
    isManager: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isManager) {
            ButtonCard(
                modifier = Modifier.border(
                    1.dp,
                    Main500,
                    shape = RoundedCornerShape(corner = CornerSize(16.dp))
                ),
                painter = painterResource(R.drawable.employee),
                painterDescription = stringResource(R.string.dashboard_employee),
                text = 45.toString(),   // TODO : API 연동
                subText = stringResource(R.string.dashboard_employee),
                onClick = { }
            )
        }

        // 보유 부품, 진행중 주문
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ButtonCard(
                modifier = Modifier.weight(1f),
                painter = painterResource(R.drawable.parts),
                painterDescription = stringResource(R.string.dashboard_parts_on_hand),
                text = 1234.toString(),   // TODO : API 연동
                subText = stringResource(R.string.dashboard_parts_on_hand),
                onClick = { }
            )

            ButtonCard(
                modifier = Modifier.weight(1f),
                painter = painterResource(R.drawable.orders),
                painterDescription = stringResource(R.string.dashboard_parts_in_progress),
                text = 23.toString(),   // TODO : API 연동
                subText = stringResource(R.string.dashboard_parts_in_progress),
                onClick = { }
            )
        }

        // 부족 부품, 주문 금액
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ButtonCard(
                modifier = Modifier.weight(1f),
                painter = painterResource(R.drawable.warning),
                painterDescription = stringResource(R.string.dashboard_shortage_of_parts),
                text = 19.toString(),   // TODO : API 연동
                subText = stringResource(R.string.dashboard_shortage_of_parts),
                onClick = { }
            )

            ButtonCard(
                modifier = Modifier.weight(1f),
                painter = painterResource(R.drawable.money),
                painterDescription = stringResource(R.string.dashboard_order_amount),
                text = 4123200.toString(),   // TODO : API 연동
                subText = stringResource(R.string.dashboard_order_amount),
                onClick = { }
            )
        }
    }
}

@Composable
fun ButtonCard(
    modifier: Modifier,
    painter: Painter,
    painterDescription: String,
    text: String,
    subText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundCardColor()
        ),
        onClick = { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .background(color = Main500, shape = CircleShape)
                    .padding(8.dp),
                painter = painter,
                tint = Color.White,
                contentDescription = painterDescription,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = textColor()
            )

            Text(
                text = subText,
                style = MaterialTheme.typography.bodyMedium,
                color = textSecondaryColor()
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderListSection(
    viewModel: DashboardViewModel,
    uiState: DashboardUiState,
    onNavigateOrderDetail: (Order) -> Unit,
    onNavigationOrder: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dashboard_order_recent_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = textColor()
            )

            IconButton(
                onClick = { onNavigationOrder() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_right),
                    contentDescription = stringResource(R.string.common_detail),
                    tint = textSecondaryColor()
                )
            }
        }

        when {
            uiState.dashboardLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.dashboardError != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(DashboardUiEvent.RetryDashboard) },
                        modifier = Modifier.height(200.dp)
                    )
                }
            }

            uiState.orderList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.orderList) { order ->
                        OrderItem(
                            order = order,
                            onClick = { onNavigateOrderDetail(order) }
                        )
                    }
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }
}