package com.sampoom.android.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sampoom.android.R
import com.sampoom.android.core.model.UserPosition
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.CommonTextField
import com.sampoom.android.core.util.positionToKorean

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSuccess: () -> Unit,
    onNavigateBack: () -> Unit = {},
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val nameLabel = stringResource(R.string.signup_title_name)
    val branchLabel = stringResource(R.string.signup_title_branch)
    val positionLabel = stringResource(R.string.signup_title_position)
    val errorLabel = stringResource(R.string.common_error)

    val nameFocus = remember { FocusRequester() }
    val branchFocus = remember { FocusRequester() }
    val positionFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val passwordCheckFocus = remember { FocusRequester() }

    LaunchedEffect(nameLabel, branchLabel, positionLabel, errorLabel) {
        viewModel.bindLabels(nameLabel, branchLabel, positionLabel, errorLabel)
    }

    val state by viewModel.state.collectAsState()
    val labelTextSize = 16.sp

    LaunchedEffect(state.success) {
        if (state.success) onSuccess()
    }

    val positionItems = remember { UserPosition.entries }
    var positionMenuExpanded by remember { mutableStateOf(false) }
    var vendorMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
//        snackbarHost = { CommonSnackBarHost(snackBarHostState) }
    ) { innerPadding ->
        val focusManager = LocalFocusManager.current
        Box( // 터치 감지용 컨테이너
            modifier = Modifier
                .fillMaxSize()
                .clickable( // 빈 공간 터치 시 포커스 해제
                    indication = null, // 터치 ripple 제거
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.oneline_logo),
                    contentDescription = stringResource(R.string.app_name)
                )
                Spacer(Modifier.height(48.dp))
                Text(
                    text = stringResource(R.string.signup_title_name),
                    fontSize = labelTextSize
                )
                CommonTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(nameFocus),
                    value = state.name,
                    onValueChange = { viewModel.onEvent(SignUpUiEvent.NameChanged(it)) },
                    placeholder = stringResource(R.string.signup_placeholder_name),
                    isError = state.nameError != null,
                    errorMessage = state.nameError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { branchFocus.requestFocus() })
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.signup_title_branch),
                    fontSize = labelTextSize
                )
                ExposedDropdownMenuBox(
                    expanded = vendorMenuExpanded,
                    onExpandedChange = { vendorMenuExpanded = it && !state.vendorsLoading }
                ) {
                    CommonTextField(
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth()
                            .focusRequester(branchFocus),
                        readOnly = true,
                        value = state.selectedVendor?.name ?: "",
                        onValueChange = {},
                        placeholder = stringResource(R.string.signup_placeholder_branch),
                        isError = state.branchError != null,
                        errorMessage = state.branchError,
                        singleLine = true,
                        enabled = !state.vendorsLoading
                    )
                    ExposedDropdownMenu(
                        expanded = vendorMenuExpanded,
                        onDismissRequest = { vendorMenuExpanded = false }
                    ) {
                        if (state.vendorsLoading) {
                            DropdownMenuItem(
                                text = { Text("로딩 중...") },
                                onClick = {}
                            )
                        } else {
                            state.vendors.forEach { vendor ->
                                DropdownMenuItem(
                                    text = { Text(vendor.name) },
                                    onClick = {
                                        viewModel.onEvent(SignUpUiEvent.VendorChanged(vendor))
                                        vendorMenuExpanded = false
                                        positionFocus.requestFocus()
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.signup_title_position),
                    fontSize = labelTextSize
                )
                ExposedDropdownMenuBox(
                    expanded = positionMenuExpanded,
                    onExpandedChange = { positionMenuExpanded = it }
                ) {
                    CommonTextField(
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                            .fillMaxWidth(),
                        readOnly = true,
                        value = state.position?.let { positionToKorean(it) } ?: "",
                        onValueChange = {},
                        placeholder = stringResource(R.string.signup_placeholder_position),
                        isError = state.positionError != null,
                        errorMessage = state.positionError,
                        singleLine = true
                    )
                    ExposedDropdownMenu(
                        expanded = positionMenuExpanded,
                        onDismissRequest = { positionMenuExpanded = false }
                    ) {
                        positionItems.forEach { role ->
                            DropdownMenuItem(
                                text = { Text(positionToKorean(role)) },
                                onClick = {
                                    viewModel.onEvent(SignUpUiEvent.PositionChanged(role))
                                    positionMenuExpanded = false
                                    emailFocus.requestFocus()
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.signup_title_email),
                    fontSize = labelTextSize
                )
                CommonTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(emailFocus),
                    value = state.email,
                    onValueChange = { viewModel.onEvent(SignUpUiEvent.EmailChanged(it)) },
                    placeholder = stringResource(R.string.signup_placeholder_email),
                    isError = state.emailError != null,
                    errorMessage = state.emailError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.signup_title_password),
                    fontSize = labelTextSize
                )
                CommonTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(passwordFocus),
                    value = state.password,
                    onValueChange = { viewModel.onEvent(SignUpUiEvent.PasswordChanged(it)) },
                    placeholder = stringResource(R.string.signup_placeholder_password),
                    isPassword = true,
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(onNext = { passwordCheckFocus.requestFocus() })
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.signup_title_password_check),
                    fontSize = labelTextSize
                )
                CommonTextField(
                    modifier = Modifier.fillMaxWidth().focusRequester(passwordCheckFocus)   ,
                    value = state.passwordCheck,
                    onValueChange = { viewModel.onEvent(SignUpUiEvent.PasswordCheckChanged(it)) },
                    placeholder = stringResource(R.string.signup_placeholder_password_check),
                    isPassword = true,
                    isError = state.passwordCheckError != null,
                    errorMessage = state.passwordCheckError,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(Modifier.height(48.dp))
                CommonButton(
                    onClick = { viewModel.onEvent(SignUpUiEvent.Submit) },
                    enabled = state.isValid && !state.loading,
                    modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                ) {
                    Text(
                        if (state.loading) stringResource(R.string.signup_button_signup_loading)
                        else stringResource(R.string.signup_button_signup)
                    )
                }
            }
        }
    }
}