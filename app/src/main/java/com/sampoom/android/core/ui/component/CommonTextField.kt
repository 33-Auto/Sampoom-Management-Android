package com.sampoom.android.core.ui.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class TextFieldVariant { Outlined, Filled }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    variant: TextFieldVariant = TextFieldVariant.Outlined
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val darkTheme = isSystemInDarkTheme()
    val cs = MaterialTheme.colorScheme

    val textColor = if (darkTheme) Color.White else Color.Black
    val containerColor = if (variant == TextFieldVariant.Filled) {
        if (darkTheme) Color(0xFF1C1C1E) else Color(0xFFF3F3F3)
    } else Color.Transparent

    val focusedBorderColor = cs.primary
    val unfocusedBorderColor = if (darkTheme) Color(0xFF666666) else Color(0xFFCCCCCC)

    val trailingIconView = if (isPassword) {
        @Composable {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = if (darkTheme) Color.White else Color.Black
                )
            }
        }
    } else null

    when (variant) {
        TextFieldVariant.Outlined -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(text = label, color = textColor) },
                placeholder = { Text(text = placeholder, color = textColor.copy(alpha = 0.4f)) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true,
                enabled = enabled,
                trailingIcon = trailingIconView,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = focusedBorderColor,
                    unfocusedBorderColor = unfocusedBorderColor,
                    disabledBorderColor = Color.Gray,
                    focusedLabelColor = focusedBorderColor,
                    unfocusedLabelColor = textColor.copy(alpha = 0.7f),
                    cursorColor = focusedBorderColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                shape = MaterialTheme.shapes.medium
            )
        }

        TextFieldVariant.Filled -> {
            TextField(
                value = value,
                onValueChange = onValueChange,
                label = { Text(text = label, color = textColor) },
                placeholder = { Text(text = placeholder, color = textColor.copy(alpha = 0.4f)) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true,
                enabled = enabled,
                trailingIcon = trailingIconView,
                visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    disabledContainerColor = containerColor.copy(alpha = 0.5f),
                    focusedIndicatorColor = focusedBorderColor,
                    unfocusedIndicatorColor = unfocusedBorderColor,
                    cursorColor = focusedBorderColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                shape = MaterialTheme.shapes.medium
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
                label = "이메일 입력",
                placeholder = "Example@naver.com",
                variant = TextFieldVariant.Outlined
            )
            CommonTextField(
                value = "",
                onValueChange = {},
                label = "비밀번호 입력",
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
            CommonTextField(
                value = "Example@naver.com",
                onValueChange = {},
                label = "이메일 입력",
                placeholder = "Example@naver.com",
                variant = TextFieldVariant.Filled
            )
            CommonTextField(
                value = "",
                onValueChange = {},
                label = "비밀번호 입력",
                placeholder = "비밀번호 입력",
                isPassword = true,
                variant = TextFieldVariant.Filled
            )
        }
    }
}