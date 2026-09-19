package com.example.myappcompose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myappcompose.domain.usecase.auth.GetCurrentUserUseCase
import com.example.myappcompose.domain.usecase.auth.LogoutUserUseCase
import com.example.myappcompose.ui.screen.tasklist.TaskListScreen
import com.example.myappcompose.ui.screen.login.LoginScreen
import com.example.myappcompose.ui.screen.register.RegisterScreen
import com.example.myappcompose.ui.screen.taskform.TaskFormScreen

@Composable
fun NavGraph(
    getCurrentUserUseCase: GetCurrentUserUseCase,
    logoutUserUseCase: LogoutUserUseCase
) {
    val navController = rememberNavController()
    val startDestination = if (getCurrentUserUseCase() != null) {
        Screen.TaskList.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.TaskList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.TaskList.route) {
            TaskListScreen(
                onLogout = {
                    logoutUserUseCase()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.TaskList.route) { inclusive = true }
                    }
                },
                onNavigateToForm = { taskId ->
                    navController.navigate(Screen.TaskForm.createRoute(taskId))
                }
            )
        }
        composable(
            route = Screen.TaskForm.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            TaskFormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
