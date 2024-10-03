package com.flowtrust.windychatty.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.flowtrust.windychatty.ui.common.ErrorScreen
import com.flowtrust.windychatty.ui.common.LoadingScreen
import com.flowtrust.windychatty.ui.pages.auth.AuthPage
import com.flowtrust.windychatty.ui.pages.auth.AuthViewModel
import com.flowtrust.windychatty.ui.pages.auth.code.AuthCodePage
import com.flowtrust.windychatty.ui.pages.auth.code.AuthCodeViewModel
import com.flowtrust.windychatty.ui.pages.auth.register.RegisterPage
import com.flowtrust.windychatty.ui.pages.auth.register.RegisterViewModel
import com.flowtrust.windychatty.ui.pages.main.chats.Chat
import com.flowtrust.windychatty.ui.pages.main.chats.ChatsScreen
import com.flowtrust.windychatty.ui.pages.main.chats.DialogScreen
import com.flowtrust.windychatty.ui.pages.main.profile.ProfileScreen
import com.flowtrust.windychatty.ui.pages.main.profile.ProfileUiState
import com.flowtrust.windychatty.ui.pages.main.profile.ProfileViewModel
import com.google.gson.Gson
import java.net.URLDecoder

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val startDestination = NavigationRoutes.AuthGraph.route
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        authGraph(navController = navController)
        mainGraph(navController = navController)
    }
}

private fun NavGraphBuilder.authGraph(
    navController: NavController,
) {
    navigation(
        route = NavigationRoutes.AuthGraph.route,
        startDestination = NavigationRoutes.AuthPhone.route
    ) {
        composable(NavigationRoutes.AuthPhone.route) {
            // Получаем экземпляр AuthViewModel
            val authViewModel: AuthViewModel = hiltViewModel()

            // Передаем ViewModel в AuthPage
            AuthPage(
                viewModel = authViewModel,
                onNavigateToCode = {
                    val phone = authViewModel.uiState.value.phone // Номер телефона
                    val code = authViewModel.uiState.value.code   // Код страны
                    // Навигация с передачей данных
                    navController.navigate(NavigationRoutes.AuthCode.createRoute(phone, code))
                },
                skipAuth = {
                    navController.navigate(NavigationRoutes.MainGraph.route) {  // Если действительный рефреш токен
                        popUpTo(0)
                    }
                }
            )
        }

        composable(NavigationRoutes.AuthCode.route,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("code") { type = NavType.StringType }
            )) { backStackEntry ->
            val authCodeViewModel: AuthCodeViewModel = hiltViewModel()

            // Получаем аргументы из backStackEntry
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""

            // Передаем эти данные в AuthCodePage
            AuthCodePage(
                viewModel = authCodeViewModel,
                phone = phone,
                code = code,
                goForward = { navController.navigate(NavigationRoutes.MainGraph.route) { popUpTo(0) } },
                onNavigateToRegister = {
                    navController.navigate(NavigationRoutes.Register.createRoute(phone, code))
                }
            )
        }

        composable(NavigationRoutes.Register.route) { backStackEntry ->
            val viewModel: RegisterViewModel = hiltViewModel()
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val code = backStackEntry.arguments?.getString("code") ?: ""

            RegisterPage(
                viewModel = viewModel,
                phone = "+$code$phone",
                onNavigateToAuthPhone = {
                    navController.navigate(NavigationRoutes.AuthPhone.route)
                },
                onNavigateToMainMenu = {
                    navController.navigate(NavigationRoutes.MainGraph.route) { popUpTo(0) }
                }
            )
        }
    }
}

private fun NavGraphBuilder.mainGraph(
    navController: NavController,
) {

    navigation(
        route = NavigationRoutes.MainGraph.route,
        startDestination = NavigationRoutes.MainChats.route
    ) {
        composable(NavigationRoutes.MainChats.route) {
            ChatsScreen(navController)
        }
        composable(
            route = "${NavigationRoutes.MainDialog.route}/{chatJson}",
            arguments = listOf(navArgument("chatJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val gson = Gson()
            val chatJson = backStackEntry.arguments?.getString("chatJson") ?: ""
            val decodedChatJson = URLDecoder.decode(chatJson, "UTF-8")
            val chat = gson.fromJson(decodedChatJson, Chat::class.java)

            DialogScreen(chat,
                goBack = { navController.popBackStack() })
        }
        composable(NavigationRoutes.MainProfile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            when (uiState) {
                is ProfileUiState.Loading -> {
                    LoadingScreen(modifier = Modifier)
                }

                is ProfileUiState.Error -> {
                    ErrorScreen(
                        description = "Повторите попытку позже",
                        onButtonClick = { navController.popBackStack() })
                }

                is ProfileUiState.Success -> {
                    ProfileScreen(
                        onBack = { navController.popBackStack() },
                        userData = (uiState as ProfileUiState.Success).userData,
                        onSave = { userData,avatar -> viewModel.updateData(userData,avatar) })
                }
            }

        }

    }
}