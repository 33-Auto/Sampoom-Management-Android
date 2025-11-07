package com.sampoom.android.feature.user.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.CommonTextField
import com.sampoom.android.feature.user.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileBottomSheet(
    user: User,
    onDismiss: () -> Unit,
    onProfileUpdated: (User) -> Unit = {},
    viewModel: UpdateProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var userName by rememberSaveable { mutableStateOf(user.userName) }

    val errorLabel = stringResource(R.string.common_error)
    val updateProfileLabel = stringResource(R.string.setting_edit_profile_edited)

    LaunchedEffect(user) {
        viewModel.onEvent(UpdateProfileUiEvent.Initialize(user))
        userName = user.userName
    }

    LaunchedEffect(errorLabel, updateProfileLabel) {
        viewModel.bindLabel(errorLabel, updateProfileLabel)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            val updatedUser = uiState.user
            if (updatedUser != null) onProfileUpdated(updatedUser)
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
        CommonTextField(
            modifier = Modifier.fillMaxWidth(),
            value = userName,
            onValueChange = { userName = it },
            placeholder = stringResource(R.string.setting_edit_profile_placeholder_username),
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        CommonButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading && userName.isNotBlank() && userName != user.userName,
            onClick = { viewModel.onEvent(UpdateProfileUiEvent.UpdateProfile(userName)) }
        ) {
            Text(stringResource(R.string.setting_edit_profile))
        }
    }
}