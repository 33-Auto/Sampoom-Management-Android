package com.sampoom.android.feature.outbound.ui

import android.util.Log.v
import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.ButtonSize
import com.sampoom.android.core.ui.component.ButtonVariant
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.EmptyContent
import com.sampoom.android.core.ui.component.ErrorContent
import com.sampoom.android.core.ui.theme.FailRed
import com.sampoom.android.core.ui.theme.Main500
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.feature.outbound.domain.model.OutboundPart
import com.sampoom.android.feature.part.ui.PartDetailUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutboundListScreen(
    viewModel: OutboundListViewModel = hiltViewModel()
) {
    val errorLabel = stringResource(R.string.common_error)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(errorLabel) {
        viewModel.bindLabel(errorLabel)
        viewModel.onEvent(OutboundListUiEvent.LoadOutboundList)
    }

    Column(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.outbound_title),
            style = MaterialTheme.typography.titleLarge,
            color = textColor(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )

        when {
            uiState.outboundLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.outboundError != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ErrorContent(
                        onRetry = { viewModel.onEvent(OutboundListUiEvent.RetryOutboundList) },
                        modifier = Modifier.height(200.dp)
                    )
                }
            }

            uiState.outboundList.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyContent(
                        message = stringResource(R.string.outbound_empty_outbound),
                        modifier = Modifier.height(200.dp)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    uiState.outboundList.forEach { category ->
                        category.groups.forEach { group ->
                            item {
                                OutboundSection(
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
                }
            }
        }
    }
}

@Composable
private fun OutboundSection(
    categoryName: String,
    groupName: String,
    parts: List<OutboundPart>,
    isUpdating: Boolean,
    isDeleting: Boolean,
    onEvent: (OutboundListUiEvent) -> Unit
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
            OutboundPartItem(
                part = part,
                isUpdating = isUpdating,
                isDeleting = isDeleting,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun OutboundPartItem(
    part: OutboundPart,
    isUpdating: Boolean,
    isDeleting: Boolean,
    onEvent: (OutboundListUiEvent) -> Unit
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
                        onEvent(OutboundListUiEvent.DeleteOutbound(part.outboundId))
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
                                OutboundListUiEvent.UpdateQuantity(
                                    part.outboundId,
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
                                OutboundListUiEvent.UpdateQuantity(
                                    part.outboundId,
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

            // 오른쪽: 삭제 버튼과 수량 조절
            /*Column(
                horizontalAlignment = Alignment.End
            ) {
                // 삭제 버튼
                IconButton(
                    onClick = { *//* 삭제 로직 *//* }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "삭제",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

                // 수량 조절 컨트롤
                QuantityControl(
                    quantity = part.quantity,
                    onQuantityChange = { newQuantity ->
                        // 수량 변경 로직
                    }
                )
            }*/
        }
    }
}