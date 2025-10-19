package com.sampoom.android.feature.cart.ui

import android.R.attr.onClick
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import com.sampoom.android.feature.cart.domain.model.CartPart
import com.sampoom.android.feature.outbound.ui.OutboundListUiEvent
import kotlin.collections.forEach

@Composable
fun CartListScreen(
    viewModel: CartListViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showEmptyCartDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.clearSuccess()
    }

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
        viewModel.onEvent(CartListUiEvent.LoadCartList)
    }

    LaunchedEffect(uiState.isOrderSuccess) {
        if (uiState.isOrderSuccess) {
            Toast.makeText(
                context,
                context.getString(R.string.cart_toast_order_text),
                Toast.LENGTH_SHORT
            ).show()
        }
        viewModel.clearSuccess()
    }

    Column(Modifier.fillMaxSize()) {
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
                text = stringResource(R.string.cart_title),
                style = MaterialTheme.typography.titleLarge,
                color = textColor()
            )

            when {
                uiState.cartLoading -> {}
                uiState.cartError != null -> {}
                uiState.cartList.isEmpty() -> {}
                else -> {
                    TextButton(
                        onClick = { showEmptyCartDialog = true }
                    ) {
                        Text(
                            text = stringResource(R.string.cart_empty_list),
                            style = MaterialTheme.typography.titleMedium,
                            color = FailRed
                        )
                    }
                }
            }
        }


        when {
            uiState.cartLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.cartError != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(CartListUiEvent.RetryCartList) },
                        modifier = Modifier.height(200.dp)
                    )
                }
            }

            uiState.cartList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyContent(
                        message = stringResource(R.string.cart_empty_outbound),
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
                        uiState.cartList.forEach { category ->
                            category.groups.forEach { group ->
                                item {
                                    CartSection(
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
                        variant = ButtonVariant.Primary,
                        size = ButtonSize.Large,
                        onClick = { showConfirmDialog = true }
                    ) { Text(stringResource(R.string.cart_order_parts)) }
                }
            }
        }
    }

    if (showEmptyCartDialog) {
        AlertDialog(
            onDismissRequest = { showEmptyCartDialog = false },
            text = { Text(stringResource(R.string.cart_dialog_empty_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showEmptyCartDialog = false
                        viewModel.onEvent(CartListUiEvent.DeleteAllCart)
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

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            text = { Text(stringResource(R.string.cart_dialog_order_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.onEvent(CartListUiEvent.ProcessOrder)
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
private fun CartSection(
    categoryName: String,
    groupName: String,
    parts: List<CartPart>,
    isUpdating: Boolean,
    isDeleting: Boolean,
    onEvent: (CartListUiEvent) -> Unit
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
            CartPartItem(
                part = part,
                isUpdating = isUpdating,
                isDeleting = isDeleting,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun CartPartItem(
    part: CartPart,
    isUpdating: Boolean,
    isDeleting: Boolean,
    onEvent: (CartListUiEvent) -> Unit
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
                        onEvent(CartListUiEvent.DeleteCart(part.cartItemId))
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
                                CartListUiEvent.UpdateQuantity(
                                    part.cartItemId,
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
//                    OutlinedTextField(
//                        value = part.quantity.toString(),
//                        onValueChange = { newValue ->
//                            when {
//                                newValue.isEmpty() -> onEvent(OutboundListUiEvent.UpdateQuantity(part.outboundId, 1))
//                                newValue == "0" -> onEvent(OutboundListUiEvent.UpdateQuantity(part.outboundId, 1))
//                                else -> {
//                                    val newQuantity = newValue.toLongOrNull()
//                                    if (newQuantity != null && newQuantity > 0) onEvent(OutboundListUiEvent.UpdateQuantity(part.outboundId, newQuantity))
//                                    else onEvent(OutboundListUiEvent.UpdateQuantity(part.outboundId, 1))
//                                }
//                            }
//                        },
//                        modifier = Modifier.width(100.dp),
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedBorderColor = Color.Transparent,
//                            unfocusedBorderColor = Color.Transparent,
//                            disabledBorderColor = Color.Transparent,
//                            errorBorderColor = Color.Transparent
//                        )
//                    )
                    CommonButton(
                        variant = ButtonVariant.Neutral,
                        size = ButtonSize.Large,
                        onClick = {
                            onEvent(
                                CartListUiEvent.UpdateQuantity(
                                    part.cartItemId,
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
        }
    }
}