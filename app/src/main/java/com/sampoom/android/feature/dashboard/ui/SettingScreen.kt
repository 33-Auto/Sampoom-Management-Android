package com.sampoom.android.feature.dashboard.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.theme.FailRed
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.disableColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.core.ui.theme.textSecondaryColor
import com.sampoom.android.core.util.formatDate
import com.sampoom.android.core.util.positionToKorean
import com.sampoom.android.feature.user.domain.model.User
import com.sampoom.android.feature.user.ui.UpdateProfileBottomSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val errorLabel = stringResource(R.string.common_error)
    val nameLabel = stringResource(R.string.signup_title_name)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val pullRefreshState = rememberPullToRefreshState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditProfileSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(true)

    LaunchedEffect(errorLabel, nameLabel) {
        viewModel.bindLabel(errorLabel, nameLabel)
    }

    LaunchedEffect(uiState.profileChangeSuccess) {
        if (uiState.profileChangeSuccess) {
            viewModel.clearSuccess()
            viewModel.onEvent(SettingUiEvent.LoadProfile)
        }
    }

    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = {
            viewModel.onEvent(SettingUiEvent.LoadProfile)
            viewModel.refreshUser()
        },
        state = pullRefreshState,
        modifier = Modifier.fillMaxSize(),
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = uiState.loading,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = pullRefreshState
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.setting_title)) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_back),
                                contentDescription = stringResource(R.string.nav_back)
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    UserSection(
                        user = user,
                        modifier = Modifier
                    )
                }
                item {
                    SettingSection(
                        onEditProfileClick = { showEditProfileSheet = true },
                        onLogoutClick = { showLogoutDialog = true }
                    )
                }
            }
        }
    }

    if (showEditProfileSheet && user != null) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    showEditProfileSheet = false
                    sheetState.hide()
                }
            },
            sheetState = sheetState
        ) {
            UpdateProfileBottomSheet(
                user = user!!,
                onProfileUpdated = {
                    viewModel.refreshUser()
                },
                onDismiss = {
                    coroutineScope.launch {
                        showEditProfileSheet = false
                        sheetState.hide()
                    }
                }
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            text = { Text(stringResource(R.string.setting_dialog_logout)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    }
                ) {
                    Text(stringResource(R.string.common_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text(stringResource(R.string.common_cancel))
                }
            }
        )
    }
}

/** 프로필 조회 섹션 */
@Composable
fun UserSection(
    user: User?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = user?.userName ?: "",
                style = MaterialTheme.typography.headlineLarge,
                color = textColor()
            )
            Text(
                text = positionToKorean(user?.position),
                style = MaterialTheme.typography.bodyMedium,
                color = textSecondaryColor()
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = user?.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = textSecondaryColor()
            )
            user?.startedAt?.takeIf { it.isNotBlank() }?.let { startedAt ->
                Text(
                    text = formatDate(startedAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondaryColor()
                )
            }
            user?.endedAt?.takeIf { it.isNotBlank() }?.let { endedAt ->
                Text(
                    text = formatDate(endedAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondaryColor()
                )
            }
        }
    }
}

/** 설정 항목 섹션 */
@Composable
private fun SettingSection(
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Card(
        onClick = { onEditProfileClick() },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundCardColor()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.setting_edit_profile),
                style = MaterialTheme.typography.titleMedium,
                color = textColor()
            )

            Icon(
                painterResource(R.drawable.chevron_right),
                contentDescription = stringResource(R.string.common_detail),
                tint = disableColor()
            )
        }
    }

    Spacer(Modifier.height(8.dp))

    Card(
        onClick = { onLogoutClick() },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundCardColor()),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.setting_logout),
                style = MaterialTheme.typography.titleMedium,
                color = FailRed
            )

            Icon(
                painterResource(R.drawable.chevron_right),
                contentDescription = stringResource(R.string.common_detail),
                tint = disableColor()
            )
        }
    }
}