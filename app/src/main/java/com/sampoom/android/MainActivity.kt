package com.sampoom.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sampoom.android.app.navigation.AppNavHost
import com.sampoom.android.core.ui.theme.SampoomManagementTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampoomManagementTheme {
                AppNavHost()
            }
        }
    }
}