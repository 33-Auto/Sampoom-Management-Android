package com.sampoom.android.feature.part.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.ButtonSize
import com.sampoom.android.core.ui.component.ButtonVariant
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.feature.part.domain.model.Part

@Composable
fun PartDetailBottomSheet(
    part: Part,
    onDismiss: () -> Unit,
    viewModel: PartDetailViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)
    val addOutboundLabel = stringResource(R.string.outbound_toast_success)
    val addCartLabel = stringResource(R.string.cart_toast_success)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showOutboundDialog by remember { mutableStateOf(false) }
    var showCartDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.clearStatus()
    }

    LaunchedEffect(errorLabel, addOutboundLabel, addCartLabel) {
        viewModel.bindLabel(errorLabel, addOutboundLabel, addCartLabel)
    }

    LaunchedEffect(part.partId) {
        viewModel.onEvent(PartDetailUiEvent.Initialize(part))
    }

    // 성공 시 Toast 표시 후 다이얼로그 닫기
    LaunchedEffect(uiState.isOutboundSuccess) {
        if (uiState.isOutboundSuccess) {
            viewModel.clearStatus()
            onDismiss()
        }
    }

    // 성공 시 Toast 표시 후 다이얼로그 닫기
    LaunchedEffect(uiState.isCartSuccess) {
        if (uiState.isCartSuccess) {
            viewModel.clearStatus()
            onDismiss()
        }
    }

    LaunchedEffect(uiState.updateError) {
        if (uiState.updateError != null) {
            viewModel.clearStatus()
            onDismiss()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
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
            Text(
                stringResource(R.string.part_current_quantity) +
                        part.quantity.toString() +
                        stringResource(R.string.common_EA)
            )
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
                    onClick = { viewModel.onEvent(PartDetailUiEvent.DecreaseQuantity) },
                    enabled = uiState.quantity > 1 && !uiState.isUpdating
                ) {
                    Text(
                        text = stringResource(R.string.part_minus),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                OutlinedTextField(
                    value = uiState.quantity.toString(),
                    onValueChange = { newValue ->
                        when {
                            newValue.isEmpty() -> viewModel.onEvent(PartDetailUiEvent.SetQuantity(1))
                            newValue == "0" -> viewModel.onEvent(PartDetailUiEvent.SetQuantity(1))
                            else -> {
                                val newQuantity = newValue.toLongOrNull()
                                if (newQuantity != null && newQuantity > 0) viewModel.onEvent(
                                    PartDetailUiEvent.SetQuantity(newQuantity)
                                )
                                else viewModel.onEvent(PartDetailUiEvent.SetQuantity(1))
                            }
                        }
                    },
                    modifier = Modifier.width(100.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent
                    )
                )
                CommonButton(
                    variant = ButtonVariant.Neutral,
                    size = ButtonSize.Large,
                    onClick = { viewModel.onEvent(PartDetailUiEvent.IncreaseQuantity) },
                    enabled = !uiState.isUpdating
                ) {
                    Text(
                        text = stringResource(R.string.part_plus),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            CommonButton(
                modifier = Modifier.weight(1F),
                variant = ButtonVariant.Error,
                size = ButtonSize.Large,
                leadingIcon = {
                    Icon(
                        painterResource(R.drawable.outbound),
                        contentDescription = null
                    )
                },
                onClick = { showOutboundDialog = true }
            ) { Text(stringResource(R.string.part_add_delivery)) }
            Spacer(Modifier.width(8.dp))
            CommonButton(
                modifier = Modifier.weight(1F),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large,
                leadingIcon = { Icon(painterResource(R.drawable.cart), contentDescription = null) },
                onClick = { showCartDialog = true }
            ) { Text(stringResource(R.string.part_add_cart)) }
        }
    }

    // 확인 다이얼로그
    if (showOutboundDialog) {
        AlertDialog(
            onDismissRequest = { showOutboundDialog = false },
            text = { Text(stringResource(R.string.outbound_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showOutboundDialog = false
                        viewModel.onEvent(
                            PartDetailUiEvent.AddToOutbound(
                                partId = part.partId,
                                quantity = uiState.quantity
                            )
                        )
                    }
                ) {
                    Text(stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showOutboundDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }

    if (showCartDialog) {
        AlertDialog(
            onDismissRequest = { showCartDialog = false },
            text = { Text(stringResource(R.string.cart_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCartDialog = false
                        viewModel.onEvent(
                            PartDetailUiEvent.AddToCart(
                                partId = part.partId,
                                quantity = uiState.quantity
                            )
                        )
                    }
                ) {
                    Text(stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCartDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }
}