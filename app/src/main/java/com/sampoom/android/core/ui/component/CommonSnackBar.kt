package com.sampoom.android.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sampoom.android.R
import com.sampoom.android.core.ui.theme.FailRed
import com.sampoom.android.core.ui.theme.White
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.textColor

@Composable
fun rememberCommonSnackBarHostState(): SnackbarHostState = remember { SnackbarHostState() }

@Composable
fun TopSnackBarHost(
    hostState: SnackbarHostState,
    extraTopPadding: Dp = 16.dp,
    showDismissButton: Boolean = true,
    isError: Boolean = false
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
                    color = if (isError) {
                        FailRed
                    } else {
                        backgroundCardColor()
                    },
                    contentColor = if (isError) White else textColor(),
                    shadowElevation = 4.dp,
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
                                    contentDescription = stringResource(R.string.common_close),
                                    tint = if (isError) White else textColor()
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}


