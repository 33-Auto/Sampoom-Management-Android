package com.sampoom.android.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
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
import com.sampoom.android.core.ui.theme.Main500
import com.sampoom.android.core.ui.theme.White
import com.sampoom.android.core.ui.theme.disableColor
import com.sampoom.android.core.ui.theme.textSecondaryColor

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
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: ButtonVariant = ButtonVariant.Primary,
    size: ButtonSize = ButtonSize.Large,
    leadingIcon: (@Composable (() -> Unit))? = null,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
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
                    containerColor = Main500,
                    contentColor = White,
                    disabledContainerColor = disableColor(),
                    disabledContentColor = textSecondaryColor()
                )
            ) {
                if (leadingIcon != null) leadingIcon()
                content()
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
                    containerColor = Main500,
                    contentColor = White,
                    disabledContainerColor = disableColor(),
                    disabledContentColor = textSecondaryColor()
                )
            ) {
                if (leadingIcon != null) leadingIcon()
                content()
            }
        }

        // Outlined with primary border
        ButtonVariant.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                modifier = modifier.height(height),
                border = BorderStroke(1.dp, Main500),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Main500,
                    disabledContentColor = textSecondaryColor()
                )
            ) {
                if (leadingIcon != null) leadingIcon()
                content()
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
                    contentColor = textSecondaryColor(),
                    disabledContentColor = textSecondaryColor()
                )
            ) {
                if (leadingIcon != null) leadingIcon()
                content()
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
                    disabledContainerColor = disableColor(),
                    disabledContentColor = textSecondaryColor()
                )
            ) {
                if (leadingIcon != null) leadingIcon()
                content()
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
        content = { Text("Button", fontWeight = FontWeight.Bold) },
        variant = ButtonVariant.Primary,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Primary_WithIcon() {
    CommonButton(
        content = { Text("Button", fontWeight = FontWeight.Bold) },
        variant = ButtonVariant.Primary,
        leadingIcon = { Icon(painterResource(android.R.drawable.ic_menu_call), contentDescription = null) },
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Tonal() {
    CommonButton(
        content = { Text("Button", fontWeight = FontWeight.Bold) },
        variant = ButtonVariant.Secondary,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Outlined() {
    CommonButton(
        content = { Text("Button", fontWeight = FontWeight.Bold) },
        variant = ButtonVariant.Outlined,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Ghost() {
    CommonButton(
        content = { Text("Button", fontWeight = FontWeight.Bold) },
        variant = ButtonVariant.Ghost,
        onClick = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F10)
@Composable
private fun CommonButtonPreview_Neutral_Disabled() {
    CommonButton(
        content = { Text("Button", fontWeight = FontWeight.Bold) },
        variant = ButtonVariant.Neutral,
        enabled = false,
        onClick = {}
    )
}