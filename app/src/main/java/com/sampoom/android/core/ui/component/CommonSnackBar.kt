package com.sampoom.android.core.ui.component

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.sampoom.android.R
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

@Composable
fun rememberCommonSnackBarHostState(): SnackbarHostState = remember { SnackbarHostState() }

@Composable
fun ShowErrorSnackBar(
    errorMessage: String?,
    snackBarHostState: SnackbarHostState,
    onConsumed: () -> Unit,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    LaunchedEffect(errorMessage) {
        val message = errorMessage ?: return@LaunchedEffect
        val result = snackBarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = true,
            duration = duration
        )
        if (result == SnackbarResult.ActionPerformed) {
            onAction?.invoke()
        }
        onConsumed()
    }
}

@Composable
fun TopSnackBarHost(
    hostState: SnackbarHostState,
    extraTopPadding: Dp = 0.dp,
    showDismissButton: Boolean = true
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        SnackbarHost(
            hostState = hostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = statusBarTop + extraTopPadding, start = 16.dp, end = 16.dp),
            snackbar = { data ->
                Surface(
                    color = backgroundCardColor(),
                    contentColor = textColor(),
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 6.dp,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(data.visuals.message)
                        Spacer(Modifier.width(12.dp))
                        val label = data.visuals.actionLabel
                        if (label != null) {
                            TextButton(onClick = { data.performAction() }) {
                                Text(text = label, color = textColor())
                            }
                            Spacer(Modifier.width(8.dp))
                        }
                        if (showDismissButton) {
                            IconButton(onClick = { data.dismiss() }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.outline_close),
                                    contentDescription = null,
                                    tint = textColor()
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}


