package com.sampoom.android.feature.user.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
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
import com.sampoom.android.core.model.EmployeeStatus
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.CommonTextField
import com.sampoom.android.core.util.employeeStatusToKorean
import com.sampoom.android.feature.user.domain.model.Employee

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateEmployeeStatusBottomSheet(
    employee: Employee,
    onDismiss: () -> Unit,
    onStatusUpdated: (Employee) -> Unit = {},
    viewModel: UpdateEmployeeStatusViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedStatus by rememberSaveable { mutableStateOf(employee.employeeStatus) }
    var employeeStatusMenuExpanded by remember { mutableStateOf(false) }

    val errorLabel = stringResource(R.string.common_error)
    val editEmployeeLabel = stringResource(R.string.employee_edit_status_edited)

    LaunchedEffect(employee) {
        viewModel.onEvent(UpdateEmployeeStatusUiEvent.Initialize(employee))
        selectedStatus = employee.employeeStatus
    }

    LaunchedEffect(errorLabel, editEmployeeLabel) {
        viewModel.bindLabel(errorLabel, editEmployeeLabel)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            uiState.employee?.let(onStatusUpdated)
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
            expanded = employeeStatusMenuExpanded,
            onExpandedChange = { employeeStatusMenuExpanded = it }
        ) {
            CommonTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    ),
                readOnly = true,
                value = employeeStatusToKorean(selectedStatus),
                onValueChange = {},
                placeholder = stringResource(R.string.employee_placeholder_status_edit),
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = employeeStatusMenuExpanded,
                onDismissRequest = { employeeStatusMenuExpanded = false }
            ) {
                EmployeeStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(employeeStatusToKorean(status)) },
                        onClick = {
                            selectedStatus = status
                            employeeStatusMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && selectedStatus != employee.employeeStatus,
            onClick = { viewModel.onEvent(UpdateEmployeeStatusUiEvent.EditEmployeeStatus(selectedStatus)) }
        ) {
            Text(stringResource(R.string.common_confirm))
        }
    }
}