package com.sampoom.android.feature.order.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.StatusChip
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.disableColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.core.util.formatWon
import com.sampoom.android.feature.order.domain.model.Order
import com.sampoom.android.feature.order.domain.model.OrderPart
import com.sampoom.android.feature.order.domain.model.subtotal
import com.sampoom.android.feature.order.domain.model.totalCost
import kotlin.collections.forEach

@Composable
fun OrderDetailContent(
    order: Order,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OrderInfoCard(order = order)
        }
        item {
            Text(
                text = stringResource(R.string.order_detail_order_items_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = textColor()
            )
        }
        order.items.forEach { category ->
            category.groups.forEach { group ->
                item {
                    OrderSection(
                        categoryName = category.categoryName,
                        groupName = group.groupName,
                        parts = group.parts
                    )
                }
            }
        }
        item { Spacer(Modifier.height(50.dp).fillMaxWidth()) }
    }
}


@Composable
private fun OrderInfoCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundCardColor())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OrderInfoRow(
                label = stringResource(R.string.order_detail_order_number),
                value = order.orderNumber ?: stringResource(R.string.common_slash)
            )
            OrderInfoRow(
                label = stringResource(R.string.order_detail_order_date),
                value = order.createdAt ?: stringResource(R.string.common_slash)
            )
            OrderInfoRow(
                label = stringResource(R.string.order_detail_order_agency),
                value = order.agencyName ?: stringResource(R.string.common_slash)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.order_detail_order_status),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondaryColor()
                )

                StatusChip(status = order.status)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.order_detail_total_amount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondaryColor()
                )
                Text(
                    text = formatWon(order.totalCost),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun OrderInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = textSecondaryColor()
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor()
        )
    }
}

@Composable
private fun OrderSection(
    categoryName: String,
    groupName: String,
    parts: List<OrderPart>
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
            OrderPartItem(part = part)
        }
    }
}

@Composable
private fun OrderPartItem(
    part: OrderPart
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
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1F)
                ) {
                    Text(
                        text = part.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor()
                    )
                    Text(
                        text = part.code,
                        style = MaterialTheme.typography.bodySmall,
                        color = textSecondaryColor()
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatWon(part.standardCost),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "x ${part.quantity}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.padding(4.dp))
            Divider(Modifier.background(textSecondaryColor()))
            Spacer(Modifier.padding(4.dp))

            Text(
                text = formatWon(part.subtotal),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}