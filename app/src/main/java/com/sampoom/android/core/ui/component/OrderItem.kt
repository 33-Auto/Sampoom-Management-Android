package com.sampoom.android.core.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sampoom.android.R
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.core.util.buildOrderTitle
import com.sampoom.android.core.util.formatDate
import com.sampoom.android.feature.order.domain.model.Order

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderItem(
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