package com.sampoom.android.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sampoom.android.R
import com.sampoom.android.core.ui.theme.FailRed
import com.sampoom.android.core.ui.theme.SuccessGreen
import com.sampoom.android.core.ui.theme.WaitYellow
import com.sampoom.android.feature.order.domain.model.OrderStatus

@Composable
fun StatusChip(status: OrderStatus) {
    val (text, color) = when (status) {
        OrderStatus.PENDING -> stringResource(R.string.order_status_pending) to WaitYellow  // 대기중
        OrderStatus.CONFIRMED -> stringResource(R.string.order_status_confirmed) to WaitYellow    // 주문 확인
        OrderStatus.SHIPPING -> stringResource(R.string.order_status_shipping) to WaitYellow // 배송 중
        OrderStatus.DELAYED -> stringResource(R.string.order_status_delayed) to WaitYellow  // 배송 지연
        OrderStatus.PRODUCING -> stringResource(R.string.order_status_producing) to WaitYellow    // 생산 중
        OrderStatus.ARRIVED -> stringResource(R.string.order_status_arrived) to WaitYellow  // 배송 완료
        OrderStatus.COMPLETED -> stringResource(R.string.order_status_completed) to SuccessGreen    // 입고 완료
        OrderStatus.CANCELED -> stringResource(R.string.order_status_canceled) to FailRed   // 주문 취소
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.2F)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}