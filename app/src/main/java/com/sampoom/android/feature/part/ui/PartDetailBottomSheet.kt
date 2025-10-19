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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    viewModel: PartDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(part) {
        viewModel.onEvent(PartDetailUiEvent.Initialize(part))
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
                        painterResource(R.drawable.delivery),
                        contentDescription = null
                    )
                },
                onClick = {}    // TODO: API 연동
            ) { Text(stringResource(R.string.part_add_delivery)) }
            Spacer(Modifier.width(8.dp))
            CommonButton(
                modifier = Modifier.weight(1F),
                variant = ButtonVariant.Primary,
                size = ButtonSize.Large,
                leadingIcon = { Icon(painterResource(R.drawable.cart), contentDescription = null) },
                onClick = {}    // TODO: API 연동
            ) { Text(stringResource(R.string.part_add_cart)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PartDetailBottomSheetPreview() {
    PartDetailBottomSheet(
        part = Part(
            partId = 1,
            name = "엔진",
            code = "ENG",
            quantity = 12
        )
    )
}