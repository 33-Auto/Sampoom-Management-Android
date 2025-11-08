package com.sampoom.android.feature.user.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.CommonTextField
import com.sampoom.android.core.util.positionToKorean
import com.sampoom.android.feature.user.domain.model.Employee

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmployeeBottomSheet(
    employee: Employee,
    onDismiss: () -> Unit,
    viewModel: EditEmployeeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedPosition by rememberSaveable { mutableStateOf(employee.position) }
    var positionMenuExpanded by remember { mutableStateOf(false) }

    val errorLabel = stringResource(R.string.common_error)
    val editEmployeeLabel = stringResource(R.string.employee_edit_edited)
    val deleteEmployeeLabel = stringResource(R.string.employee_edit_deleted)

    LaunchedEffect(employee) {
        viewModel.onEvent(EditEmployeeUiEvent.Initialize(employee))
        selectedPosition = employee.position
    }

    LaunchedEffect(errorLabel, editEmployeeLabel, deleteEmployeeLabel) {
        viewModel.bindLabel(errorLabel, editEmployeeLabel, deleteEmployeeLabel)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.clearStatus()
            onDismiss()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = positionMenuExpanded,
            onExpandedChange = { positionMenuExpanded = it }
        ) {
            CommonTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    ),
                readOnly = true,
                value = positionToKorean(selectedPosition),
                onValueChange = {},
                placeholder = stringResource(R.string.signup_placeholder_position),
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = positionMenuExpanded,
                onDismissRequest = { positionMenuExpanded = false }
            ) {
                UserPosition.entries.forEach { position ->
                    androidx.compose.material3.DropdownMenuItem(
                        text = { Text(positionToKorean(position)) },
                        onClick = {
                            selectedPosition = position
                            positionMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && selectedPosition != employee.position,
            onClick = { viewModel.onEvent(EditEmployeeUiEvent.EditEmployee(selectedPosition)) }
        ) {
            Text(stringResource(R.string.common_confirm))
        }
    }
}