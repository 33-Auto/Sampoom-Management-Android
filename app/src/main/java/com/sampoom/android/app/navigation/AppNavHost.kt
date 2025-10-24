package com.sampoom.android.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sampoom.android.R
import com.sampoom.android.core.ui.theme.backgroundColor
import com.sampoom.android.feature.user.ui.AuthViewModel
import com.sampoom.android.feature.user.ui.LoginScreen
import com.sampoom.android.feature.user.ui.SignUpScreen
import com.sampoom.android.feature.cart.ui.CartListScreen
import com.sampoom.android.feature.order.ui.OrderDetailScreen
import com.sampoom.android.feature.order.ui.OrderListScreen
import com.sampoom.android.feature.outbound.ui.OutboundListScreen
import com.sampoom.android.feature.part.ui.PartListScreen
import com.sampoom.android.feature.part.ui.PartScreen

const val ROUTE_LOGIN = "login"
const val ROUTE_SIGNUP = "signup"
const val ROUTE_HOME = "home"

// Main Screen
const val ROUTE_DASHBOARD = "dashboard"
const val ROUTE_OUTBOUND = "outbound"
const val ROUTE_CART = "cart"
const val ROUTE_ORDERS = "orders"

// Detail Screen
const val ROUTE_PARTS = "parts"
const val ROUTE_PART_LIST = "parts/{agencyId}/group/{groupId}"
fun routePartList(agencyId: Long, groupId: Long): String = "parts/$agencyId/group/$groupId"
const val ROUTE_ORDER_DETAIL = "orders/{agencyId}/orders/{orderId}"
fun routeOrderDetail(agencyId: Long, orderId: Long): String = "orders/$agencyId/orders/$orderId"
const val ROUTE_SEARCH = "search"
const val ROUTE_EMPLOYEE = "employee"
const val ROUTE_SETTINGS = "settings"

sealed class BottomNavItem(
    val route: String,
    val title: Int,
    val icon: Int
) {
    object Dashboard : BottomNavItem(ROUTE_DASHBOARD, R.string.nav_dashboard, R.drawable.dashboard)
    object Delivery : BottomNavItem(ROUTE_OUTBOUND, R.string.nav_delivery, R.drawable.outbound)
    object Cart : BottomNavItem(ROUTE_CART, R.string.nav_cart, R.drawable.cart)
    object Orders : BottomNavItem(ROUTE_ORDERS, R.string.nav_order, R.drawable.orders)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.logoutEvent.collect {
            navController.navigate(ROUTE_LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) ROUTE_HOME else ROUTE_LOGIN,
        modifier = Modifier.background(backgroundColor())
    ) {
        composable(ROUTE_LOGIN) {
            LoginScreen(
                onSuccess = {
                    authViewModel.updateLoginState()
                    navController.navigate(ROUTE_HOME) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true } // 로그인 화면 스택 제거
                    }
                },
                onNavigateSignUp = {
                    navController.navigate(ROUTE_SIGNUP)
                })
        }
        composable(ROUTE_SIGNUP) {
            SignUpScreen(
                onSuccess = {
                    navController.navigate(ROUTE_HOME) {
                        popUpTo(ROUTE_LOGIN) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
        composable(ROUTE_HOME) { MainScreen(navController) }
        composable(ROUTE_PARTS) {
            PartScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigatePartList = { group ->
                    // TODO: 실제 사용자의 agencyId 사용
                    navController.currentBackStackEntry?.savedStateHandle?.set("groupName", group.name)
                    navController.navigate(routePartList(1, group.id))
                }
            )
        }
        composable(
            ROUTE_PART_LIST,
            arguments = listOf(
                navArgument("agencyId") { type = NavType.LongType },
                navArgument("groupId") { type = NavType.LongType }
            )
        ) {
            PartListScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                navController = navController
            )
        }
        composable(
            ROUTE_ORDER_DETAIL,
            arguments = listOf(
                navArgument("agencyId") { type = NavType.LongType },
                navArgument("orderId") { type = NavType.LongType }
            )
        ) {
            OrderDetailScreen(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
            startDestination = ROUTE_DASHBOARD
        ) {
            composable(ROUTE_DASHBOARD) {
                DashboardScreen(
                    paddingValues = innerPadding
                ) {
                    parentNavController.navigate(ROUTE_LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
            composable(ROUTE_OUTBOUND) {
                OutboundListScreen(
                    paddingValues = innerPadding
                )
            }
            composable(ROUTE_CART) {
                CartListScreen(
                    paddingValues = innerPadding
                )
            }
            composable(ROUTE_ORDERS) {
                OrderListScreen(
                    paddingValues = innerPadding,
                    onNavigateOrderDetail = { order ->
                        // TODO: 실제 사용자의 agencyId 사용
                        parentNavController.navigate(routeOrderDetail(1, order.orderId))
                    }
                )
            }
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
        Icon(
            painterResource(R.drawable.parts),
            contentDescription = stringResource(R.string.part_title),
            tint = Color.White
        )
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
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = stringResource(item.title)
                    )
                },
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
private fun DashboardScreen(
    paddingValues: PaddingValues,
    onClick: () -> Unit
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    // 홈 화면 구현
    Column(Modifier.padding(paddingValues)) {
        Text("대시보드 화면", modifier = Modifier.padding(paddingValues))

        Button(onClick = {
            authViewModel.signOut()
            onClick()
        }) {
            Text("로그아웃")
        }
    }

}