package com.sampoom.android.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sampoom.android.feature.auth.ui.LoginScreen

const val ROUTE_LOGIN = "login"

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = ROUTE_LOGIN) {
        composable(ROUTE_LOGIN) { LoginScreen(onSuccess = {
//            navController.navigate("main") {
//                popUpTo(ROUTE_LOGIN) { inclusive = true } // 로그인 화면 스택 제거
//            }
        }) }
    }
}