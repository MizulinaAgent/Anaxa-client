package com.example.anaxa.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.anaxa.ui.screens.auth.AuthScreen
import com.example.anaxa.ui.screens.home.HomeScreen
import com.example.anaxa.ui.screens.createlot.CreateLotScreen
import com.example.anaxa.ui.screens.lotdetail.LotDetailScreen
import com.example.anaxa.ui.screens.lots.LotsScreen
import com.example.anaxa.ui.screens.chat.ChatScreen
import com.example.anaxa.ui.screens.mylots.MyLotsScreen
import com.example.anaxa.ui.screens.orders.OrdersScreen

@Composable
fun AnaxaNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.AUTH) {
        composable(Routes.AUTH) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                onGameClick = { gameId -> navController.navigate(Routes.lots(gameId)) },
                onMyLotsClick = { navController.navigate(Routes.MY_LOTS) },
                onOrdersClick = { navController.navigate(Routes.ORDERS) },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onCreateLotClick = { navController.navigate(Routes.CREATE_LOT) }
            )
        }
        composable(
            Routes.LOTS,
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) {
            LotsScreen(
                onBack = { navController.popBackStack() },
                onLotClick = { lotId -> navController.navigate(Routes.lotDetail(lotId)) }
            )
        }
        composable(
            Routes.LOT_DETAIL,
            arguments = listOf(navArgument("lotId") { type = NavType.StringType })
        ) {
            LotDetailScreen(
                onBack = { navController.popBackStack() },
                onOrderCreated = { orderId ->
                    navController.navigate(Routes.chat(orderId))
                },
                onSellerClick = { userId -> navController.navigate(Routes.userProfile(userId)) }
            )
        }
        composable(Routes.CREATE_LOT) {
            CreateLotScreen(
                onBack = { navController.popBackStack() },
                onCreated = { navController.popBackStack() }
            )
        }
        composable(Routes.MY_LOTS) {
            MyLotsScreen(
                onBack = { navController.popBackStack() },
                onLotClick = { lotId -> navController.navigate(Routes.lotDetail(lotId)) }
            )
        }
        composable(Routes.ORDERS) {
            OrdersScreen(
                onBack = { navController.popBackStack() },
                onOrderClick = { orderId -> navController.navigate(Routes.chat(orderId)) }
            )
        }
        composable(
            Routes.CHAT,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) {
            ChatScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.PROFILE) { Placeholder("Профиль") }
        composable(
            Routes.USER_PROFILE,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { Placeholder("Профиль пользователя") }
    }
}

@Composable
private fun Placeholder(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(name)
    }
}
