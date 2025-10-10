package com.sampoom.android.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Sampoom common button with multiple visual variants.
 *
 * Usage
 * -----
 * CommonButton(
 *   text = "Button",
 *   variant = ButtonVariant.Primary,
 *   onClick = { ... }
 * )
 *
 * Optionally pass a leading icon:
 * CommonButton(
 *   text = "Button",
 *   variant = ButtonVariant.Primary,
 *   leadingIcon = { Icon(painterResource(R.drawable.parts), contentDescription = null) },
 *   onClick = { ... }
 * )
 */
@Composable
fun CommonButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Large,
    leadingIcon: (@Composable (() -> Unit))? = null,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.large
    val height = when (size) {
        ButtonSize.Large -> 56.dp
        ButtonSize.Medium -> 48.dp
        ButtonSize.Small -> 40.dp
    }

    when (variant) {
        ButtonVariant.Primary -> {
            Button(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                modifier = modifier.height(height),
                colors = ButtonDefaults.buttonColors(
                    containerColor = cs.primary,
                    contentColor = cs.onPrimary,
                    disabledContainerColor = cs.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = cs.onSurface.copy(alpha = 0.38f),
                )
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = if (leadingIcon != null) 8.dp else 0.dp)
                )
            }
        }

        // Light/secondary (tonal) filled button
        ButtonVariant.Secondary -> {
            FilledTonalButton(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                modifier = modifier.height(height),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = cs.secondaryContainer,
                    contentColor = cs.onSecondaryContainer,
                    disabledContainerColor = cs.onSurface.copy(alpha = 0.08f),
                    disabledContentColor = cs.onSurface.copy(alpha = 0.38f)
                )
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = if (leadingIcon != null) 8.dp else 0.dp)
                )
            }
        }

        // Outlined with primary border
        ButtonVariant.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                modifier = modifier.height(height),
                border = BorderStroke(1.dp, cs.primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = cs.primary,
                    disabledContentColor = cs.onSurface.copy(alpha = 0.38f)
                )
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Ghost: no container, subtle onSurface content
        ButtonVariant.Ghost -> {
            TextButton(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                modifier = modifier.height(height),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = cs.onSurface,
                    disabledContentColor = cs.onSurface.copy(alpha = 0.38f)
                )
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Destructive/Neutral (dark) filled – matches the black fill example
        ButtonVariant.Neutral -> {
            Button(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                modifier = modifier.height(height),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF000000),
                    contentColor = Color.White,
                    disabledContainerColor = cs.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = cs.onSurface.copy(alpha = 0.38f),
                )
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

enum class ButtonVariant {
    /** Primary filled (purple in design). Accepts optional [leadingIcon]. */
    Primary,

    /** Filled tonal (light purple in design). */
    Secondary,

    /** Outlined with primary border. */
    Outlined,

    /** No container; text only. */
    Ghost,

    /** Solid dark/neutral fill. */
    Neutral,
}

enum class ButtonSize { Large, Medium, Small }

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_All() {
    // Primary
    CommonButton(
        text = "Button",
        variant = ButtonVariant.Primary,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Primary_WithIcon() {
    CommonButton(
        text = "Button",
        variant = ButtonVariant.Primary,
        leadingIcon = { Icon(painterResource(android.R.drawable.ic_menu_call), contentDescription = null) },
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Tonal() {
    CommonButton(
        text = "Button",
        variant = ButtonVariant.Secondary,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Outlined() {
    CommonButton(
        text = "Button",
        variant = ButtonVariant.Outlined,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Ghost() {
    CommonButton(
        text = "Button",
        variant = ButtonVariant.Ghost,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Neutral_Disabled() {
    CommonButton(
        text = "Button",
        variant = ButtonVariant.Neutral,
        enabled = false,
        onClick = {}
    )
}