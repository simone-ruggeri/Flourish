package com.example.flourish.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flourish.ui.screens.homepage.HomepageScreen
import com.example.flourish.ui.screens.login.LoginScreen
import com.example.flourish.ui.screens.signup.SignupScreen
import com.example.flourish.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val startDestination = NavigationRoute.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(NavigationRoute.Login.route) {
            val loginViewModel : LoginViewModel = koinViewModel()
            LoginScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable(NavigationRoute.SignUp.route) {
            SignupScreen(
                navController = navController
            )
        }
        composable(NavigationRoute.Homepage.route) {
            HomepageScreen(
                navController = navController
            )
        }
    }
}