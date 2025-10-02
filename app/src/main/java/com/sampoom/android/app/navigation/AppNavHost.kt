package com.sampoom.android.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sampoom.android.R
import com.sampoom.android.feature.auth.ui.LoginScreen
import com.sampoom.android.feature.part.ui.PartScreen

const val ROUTE_LOGIN = "login"
const val ROUTE_HOME = "home"

// Main Screen
const val ROUTE_PART = "part"
const val ROUTE_INVENTORY = "inventory"
const val ROUTE_PROFILE = "profile"
const val ROUTE_SETTINGS = "settings"

// Detail Screen
const val ROUTE_DETAIL = "detail"

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Part : BottomNavItem(ROUTE_PART, "Part", R.drawable.outline_home_24)
    object Inventory : BottomNavItem(ROUTE_INVENTORY, "인벤토리", R.drawable.outline_home_24)
    object Profile : BottomNavItem(ROUTE_PROFILE, "프로필", R.drawable.outline_home_24)
    object Settings : BottomNavItem(ROUTE_SETTINGS, "설정", R.drawable.outline_home_24)
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // TODO: 임시 로그인 상태 확인 -> AuthRepository에서 확인하도록 변경
    val isLoggedIn = true

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) ROUTE_HOME else ROUTE_LOGIN
    ) {
        composable(ROUTE_LOGIN) {
            LoginScreen(onSuccess = {
                navController.navigate(ROUTE_HOME) {
                    popUpTo(ROUTE_LOGIN) { inclusive = true } // 로그인 화면 스택 제거
                }
            })
        }
        composable(ROUTE_HOME) { MainScreen(navController) }
        composable(ROUTE_DETAIL) { DetailScreen() }
    }
}

@Composable
fun MainScreen(
    parentNavController: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_PART,
            modifier = Modifier.background(Color.Green).padding(innerPadding)
        ) {
            composable(ROUTE_PART) { PartScreen() }
            composable(ROUTE_INVENTORY) { InventoryScreen() }
            composable(ROUTE_PROFILE) { ProfileScreen() }
            composable(ROUTE_SETTINGS) { SettingsScreen(parentNavController) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem.Part,
        BottomNavItem.Inventory,
        BottomNavItem.Profile,
        BottomNavItem.Settings,
    )

    NavigationBar {
        bottomNavItems.forEach { item ->
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// 임시 화면들 (실제로는 각각의 feature 모듈에서 구현)
@Composable
private fun InventoryScreen() {
    // 홈 화면 구현
    Text("인벤토리 화면")
}

@Composable
private fun ProfileScreen() {
    // 프로필 화면 구현
    Text("프로필 화면")
}

@Composable
private fun SettingsScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 설정 화면 구현
        Text("설정 화면")
        Button(
            onClick = { navController.navigate(ROUTE_DETAIL) }
        ) {
            Text("상세 화면")
        }
    }
}

@Composable
private fun DetailScreen() {
    // 설정 화면 구현
    Scaffold { innerPadding ->
        Box(Modifier.fillMaxSize().background(Color.Red).padding(innerPadding)
            ) {
            Text("상세 화면")
        }
    }
}