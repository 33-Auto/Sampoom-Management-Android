package com.sampoom.android.core.ui.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sampoom.android.core.ui.theme.FailRed
import com.sampoom.android.core.ui.theme.Main500
import com.sampoom.android.core.ui.theme.backgroundColor

enum class TextFieldVariant { Outlined, Filled }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    variant: TextFieldVariant = TextFieldVariant.Outlined,
    isError: Boolean = false,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions(),
    readOnly: Boolean = false,
    singleLine: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val containerColor = if (variant == TextFieldVariant.Filled) backgroundColor() else Color.Transparent

    val focusedBorderColor = if (isError) FailRed else Main500
    val unfocusedBorderColor = when {
        isError -> FailRed
        isSystemInDarkTheme() -> Color(0xFF666666)
        else -> Color(0xFFCCCCCC)
    }

    val trailingIconView = if (isPassword) {
        @Composable {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = textColor
                )
            }
        }
    } else null

    // 에러 메시지 표시용
    val supportingTextView = if (isError && errorMessage != null) {
        @Composable {
            Text(
                text = errorMessage,
                color = FailRed,
                style = MaterialTheme.typography.bodySmall
            )
        }
    } else null

    when (variant) {
        TextFieldVariant.Outlined -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = placeholder, color = textColor.copy(alpha = 0.4f)) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                readOnly = readOnly,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                trailingIcon = trailingIconView,
                supportingText = supportingTextView,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text, imeAction = imeAction),
                keyboardActions = keyboardActions,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = focusedBorderColor,
                    unfocusedBorderColor = unfocusedBorderColor,
                    errorBorderColor = FailRed,
                    disabledBorderColor = Color.Gray,
                    focusedLabelColor = focusedBorderColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    cursorColor = focusedBorderColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                shape = MaterialTheme.shapes.large
            )
        }

        TextFieldVariant.Filled -> {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(text = placeholder, color = textColor.copy(alpha = 0.4f)) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true,
                enabled = enabled,
                isError = isError,
                trailingIcon = trailingIconView,
                supportingText = supportingTextView,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    errorContainerColor = FailRed,
                    disabledContainerColor = containerColor.copy(alpha = 0.5f),
                    focusedIndicatorColor = focusedBorderColor,
                    unfocusedIndicatorColor = unfocusedBorderColor,
                    cursorColor = focusedBorderColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                shape = MaterialTheme.shapes.large
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Light_CommonTextFields() {
    MaterialTheme {
        Column {
            CommonTextField(
                value = "Example@naver.com",
                onValueChange = {},
                placeholder = "Example@naver.com",
                variant = TextFieldVariant.Outlined
            )
            CommonTextField(
                value = "",
                onValueChange = {},
                placeholder = "비밀번호 입력",
                isPassword = true,
                variant = TextFieldVariant.Outlined
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Dark_CommonTextFields() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column {
            // 정상
            CommonTextField(
                value = "Example@naver.com",
                onValueChange = {},
                placeholder = "이메일",
                variant = TextFieldVariant.Outlined
            )

            // 에러
            CommonTextField(
                value = "invalid",
                onValueChange = {},
                placeholder = "이메일",
                variant = TextFieldVariant.Outlined,
                isError = true,
                errorMessage = "올바른 이메일 형식이 아닙니다"
            )

            // 비밀번호 에러
            CommonTextField(
                value = "123",
                onValueChange = {},
                placeholder = "비밀번호",
                isPassword = true,
                variant = TextFieldVariant.Outlined,
                isError = true,
                errorMessage = "비밀번호는 최소 8자 이상이어야 합니다"
            )
        }
    }
}