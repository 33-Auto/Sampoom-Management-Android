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
        OrderStatus.PENDING -> stringResource(R.string.order_status_pending) to WaitYellow
        OrderStatus.COMPLETED -> stringResource(R.string.order_status_completed) to SuccessGreen
        OrderStatus.CANCELED -> stringResource(R.string.order_status_canceled) to FailRed
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