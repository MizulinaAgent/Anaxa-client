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
                onOrdersClick = { navController.navigate(Routes.ORDERS) },
                onProfileClick = { navController.navigate(Routes.PROFILE) },
                onCreateLotClick = { navController.navigate(Routes.CREATE_LOT) }
            )
        }
        composable(
            Routes.LOTS,
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { Placeholder("Лоты") }
        composable(
            Routes.LOT_DETAIL,
            arguments = listOf(navArgument("lotId") { type = NavType.StringType })
        ) { Placeholder("Детали лота") }
        composable(Routes.CREATE_LOT) { Placeholder("Создать лот") }
        composable(Routes.MY_LOTS) { Placeholder("Мои лоты") }
        composable(Routes.ORDERS) { Placeholder("Заказы") }
        composable(
            Routes.CHAT,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { Placeholder("Чат") }
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
