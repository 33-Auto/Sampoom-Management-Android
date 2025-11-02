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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sampoom.android.R
import com.sampoom.android.core.ui.component.CommonButton
import com.sampoom.android.core.ui.component.CommonTextField
import com.sampoom.android.core.ui.theme.Main500

@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    onNavigateSignUp: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val emailLabel = stringResource(R.string.login_title_email)
    val passwordLabel = stringResource(R.string.login_title_password)
    val errorLabel = stringResource(R.string.common_error)

    LaunchedEffect(emailLabel, passwordLabel, errorLabel) {
        viewModel.bindLabel(emailLabel, passwordLabel, errorLabel)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.success) {
        if (uiState.success) onSuccess()
    }

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
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
                Modifier
                    .fillMaxSize()
                    .imePadding()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.square_logo),
                    contentDescription = stringResource(R.string.app_name)
                )
                Spacer(Modifier.height(48.dp))
                CommonTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.email,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.EmailChanged(it)) },
                    placeholder = stringResource(R.string.login_placeholder_email),
                    isError = uiState.emailError != null,
                    errorMessage = uiState.emailError
                )
                Spacer(Modifier.height(8.dp))
                CommonTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.password,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.PasswordChanged(it)) },
                    placeholder = stringResource(R.string.login_placeholder_password),
                    isPassword = true,
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError
                )
                Spacer(Modifier.height(48.dp))
                CommonButton(
                    onClick = { viewModel.onEvent(LoginUiEvent.Submit) },
                    enabled = uiState.isValid && !uiState.loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (uiState.loading) stringResource(R.string.login_button_login_loading)
                        else stringResource(R.string.login_button_login)
                    )
                }


                Spacer(Modifier.weight(1f))

                val annotatedText = buildAnnotatedString {
                    append(stringResource(R.string.login_need_account))

                    // 클릭 가능한 회원가입 텍스트
                    pushStringAnnotation(tag = "SIGNUP", annotation = "signup")
                    withStyle(
                        style = SpanStyle(
                            color = Main500, // 원하는 색상
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(stringResource(R.string.login_signup))
                    }
                    append(stringResource(R.string.login_do))
                    pop()
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = annotatedText,
                        modifier = Modifier
                            .padding(vertical = 24.dp)
                            .clickable { onNavigateSignUp() },
                        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                    )
                }
            }
        }
    }
}