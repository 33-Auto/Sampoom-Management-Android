package com.sampoom.android.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
const val ROUTE_DASHBOARD = "dashboard"
const val ROUTE_DELIVERY = "delivery"
const val ROUTE_CART = "cart"
const val ROUTE_ORDERS = "orders"

// Detail Screen
const val ROUTE_PARTS = "parts"
const val ROUTE_EMPLOYEE = "employee"
const val ROUTE_SETTINGS = "settings"

sealed class BottomNavItem(
    val route: String,
    val title: Int,
    val icon: Int
) {
    object Dashboard : BottomNavItem(ROUTE_DASHBOARD, R.string.nav_dashboard, R.drawable.dashboard)
    object Delivery : BottomNavItem(ROUTE_DELIVERY, R.string.nav_delivery, R.drawable.delivery)
    object Cart : BottomNavItem(ROUTE_CART, R.string.nav_cart, R.drawable.cart)
    object Orders : BottomNavItem(ROUTE_ORDERS, R.string.nav_order, R.drawable.orders)
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
        composable(ROUTE_PARTS) { PartScreen() }
    }
}

@Composable
fun MainScreen(
    parentNavController: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        floatingActionButton = { PartsFab(parentNavController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ROUTE_DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROUTE_DASHBOARD) { DashboardScreen() }
            composable(ROUTE_DELIVERY) { DeliveryScreen() }
            composable(ROUTE_CART) { CartScreen() }
            composable(ROUTE_ORDERS) { OrderScreen() }
        }
    }
}

@Composable
fun PartsFab(navController: NavHostController) {
    FloatingActionButton(
        onClick = {
            navController.navigate(ROUTE_PARTS) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    ) {
        Icon(painterResource(R.drawable.parts), contentDescription = stringResource(R.string.part_title))
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Delivery,
        BottomNavItem.Cart,
        BottomNavItem.Orders,
    )

    NavigationBar {
        bottomNavItems.forEach { item ->
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = stringResource(item.title)) },
                label = { Text(stringResource(item.title)) },
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
private fun DashboardScreen() {
    // 홈 화면 구현
    Text("대시보드 화면")
}

@Composable
private fun DeliveryScreen() {
    // 프로필 화면 구현
    Text("Delivery 화면")
}

@Composable
private fun CartScreen() {
    // 프로필 화면 구현
    Text("Cart 화면")
}

@Composable
private fun OrderScreen() {
    // 프로필 화면 구현
    Text("Order 화면")
}