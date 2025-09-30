package com.sampoom.android.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sampoom.android.feature.auth.ui.LoginScreen
import com.sampoom.android.feature.part.ui.PartScreen

const val ROUTE_LOGIN = "login"
const val ROUTE_PART = "part"

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = ROUTE_PART) {

        // 로그인 화면
        composable(ROUTE_LOGIN) { LoginScreen(onSuccess = {
//            navController.navigate("main") {
//                popUpTo(ROUTE_LOGIN) { inclusive = true } // 로그인 화면 스택 제거
//            }
        }) }

        // 부품 조회 화면
        composable(ROUTE_PART) { PartScreen() }
    }
}