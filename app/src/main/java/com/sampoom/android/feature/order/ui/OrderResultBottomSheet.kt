package com.sampoom.android.feature.order.ui

import android.R.attr.onClick
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.ButtonVariant
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.theme.SuccessGreen
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.model.OrderStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderResultBottomSheet(
    order: Order,
    onDismiss: () -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showCancelOrderDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(order.orderId) {
        viewModel.setOrderIdFromApi(order.orderId)
        viewModel.onEvent(OrderDetailUiEvent.LoadOrder)
    }

    val displayedOrder = uiState.orderDetail ?: order

    // 성공 시 Toast 표시 후 다이얼로그 닫기
    LaunchedEffect(uiState.isProcessingCancelSuccess) {
        if (uiState.isProcessingCancelSuccess) {
            Toast.makeText(context, context.getString(R.string.order_detail_toast_order_cancel), Toast.LENGTH_SHORT).show()
            viewModel.clearSuccess()
            viewModel.onEvent(OrderDetailUiEvent.LoadOrder)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.9f)
        ) {
            // 주문 완료 헤더
            OrderCompleteHeader()

            Spacer(modifier = Modifier.height(16.dp))

            // OrderDetailContent 재사용
            OrderDetailContent(
                order = displayedOrder,
                modifier = Modifier.weight(1f)
            )

            // 하단 고정 버튼들
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val orderStatus = displayedOrder.status
                CommonButton(
                    modifier = Modifier.weight(1f),
                    variant = ButtonVariant.Error,
                    enabled = !uiState.isProcessing &&
                            orderStatus == OrderStatus.PENDING,
                    onClick = { showCancelOrderDialog = true }
                ) {
                    Text(stringResource(R.string.order_detail_order_cancel))
                }
            }

            // 하단 여백
            Spacer(modifier = Modifier.height(16.dp))
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
}

@Composable
private fun OrderCompleteHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_circle),
            contentDescription = stringResource(R.string.common_confirm),
            tint = SuccessGreen,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.cart_toast_order_text),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = textColor()
        )
    }
}