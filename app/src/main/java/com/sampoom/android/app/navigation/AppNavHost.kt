package com.sampoom.android.app.navigation

import android.R.attr.order
import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.ComponentActivity
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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowInsetsControllerCompat
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sampoom.android.R
import com.sampoom.android.core.ui.theme.Main100
import com.sampoom.android.core.ui.theme.Main300
import com.sampoom.android.core.ui.theme.Main500
import com.sampoom.android.core.ui.theme.backgroundCardColor
import com.sampoom.android.core.ui.theme.backgroundColor
import com.sampoom.android.core.ui.theme.textColor
import com.sampoom.android.feature.user.ui.AuthViewModel
import com.sampoom.android.feature.user.ui.LoginScreen
import com.sampoom.android.feature.user.ui.SignUpScreen
import com.sampoom.android.feature.cart.ui.CartListScreen
import com.sampoom.android.feature.dashboard.ui.DashboardScreen
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

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val activity = LocalContext.current as ComponentActivity
    val homeNavColor = backgroundCardColor()
    val elseNavColor = backgroundColor()
    val lightIcons = !isSystemInDarkTheme()

    LaunchedEffect(currentRoute, homeNavColor, lightIcons) {
        if (currentRoute == ROUTE_HOME) {
            val window = activity.window
            window.navigationBarColor = homeNavColor.toArgb()
            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightNavigationBars = lightIcons
        } else {
            val window = activity.window
            window.navigationBarColor = elseNavColor.toArgb()
            WindowInsetsControllerCompat(window, window.decorView)
                .isAppearanceLightNavigationBars = lightIcons
        }
    }

    if (isLoading) { return }

    NavHost(
        navController = navController,
//        startDestination = ROUTE_HOME,
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
        composable(ROUTE_HOME) { MainScreen(navController, authViewModel) }
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
    parentNavController: NavHostController,
    authViewModel: AuthViewModel
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
                    paddingValues = innerPadding,
                    onNavigateOrderDetail = { order ->
                        parentNavController.navigate(routeOrderDetail(1, order.orderId))
                    },
                    onNavigationOrder = {
                        navController.navigate(ROUTE_ORDERS) {
                            popUpTo(ROUTE_DASHBOARD) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLogoutClick = {
                        authViewModel.signOut()
                        parentNavController.navigate(ROUTE_LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
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
        containerColor = Main500,
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

    NavigationBar(
        containerColor = backgroundCardColor(),
        contentColor = Main500,
    ) {
        bottomNavItems.forEach { item ->
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Main500,
                    unselectedIconColor = textColor(),
                    selectedTextColor = Main500,
                    unselectedTextColor = textColor(),
                    indicatorColor = Main100
                ),
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