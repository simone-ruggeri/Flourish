package com.example.flourish.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.flourish.data.preferences.UserPreferences
import com.example.flourish.ui.screens.breathing.BreathingScreen
import com.example.flourish.ui.screens.calendar.CalendarScreen
import com.example.flourish.ui.screens.chart.ChartScreen
import com.example.flourish.ui.screens.exercises.ExercisesScreen
import com.example.flourish.ui.screens.homepage.HomepageScreen
import com.example.flourish.ui.screens.login.LoadingScreen
import com.example.flourish.ui.screens.login.LoginScreen
import com.example.flourish.ui.screens.meditation.MeditationScreen
import com.example.flourish.ui.screens.mood.MoodScreen
import com.example.flourish.ui.screens.profile.ProfileScreen
import com.example.flourish.ui.screens.signup.SignupScreen
import com.example.flourish.ui.screens.sleep.SleepScreen
import com.example.flourish.viewmodel.ActivityDialogViewModel
import com.example.flourish.viewmodel.ChartViewModel
import com.example.flourish.viewmodel.LoginViewModel
import com.example.flourish.viewmodel.MoodRatingViewModel
import com.example.flourish.viewmodel.ProfileViewModel
import com.example.flourish.viewmodel.SignupViewModel
import com.example.flourish.viewmodel.SleepRatingViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    userPreferences: UserPreferences,
    modifier: Modifier = Modifier
) {
    // Stato per il userId, inizializzato con un valore di default
    val userIdState = remember { mutableStateOf<Long?>(-1L) }

    // Effetto per raccogliere i dati dal flusso userIdFlow
    LaunchedEffect(Unit) {
        userPreferences.userIdFlow.collect { userId ->
            userIdState.value = userId
        }
    }

    // Mostra LoadingScreen finché non riceviamo un valore valido per userId
    if (userIdState.value == -1L) {
        // Mostra la schermata di caricamento finché userId non è stato ricevuto
        LoadingScreen()
        return
    }

    // Mappa la schermata da mostrare in base al valore di userId
    val startDestination = if (userIdState.value == null || userIdState.value == -1L) {
        NavigationRoute.Login.route
    } else {
        NavigationRoute.Homepage.route
    }

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
            val signupViewModel: SignupViewModel = koinViewModel()
            SignupScreen(
                navController = navController,
                signupViewModel = signupViewModel
            )
        }
        composable(NavigationRoute.Homepage.route) {
            HomepageScreen(
                navController = navController
            )
        }
        composable(NavigationRoute.Sleep.route) {
            val sleepRatingViewModel: SleepRatingViewModel = koinViewModel()
            SleepScreen(
                navController = navController,
                viewModel = sleepRatingViewModel
            )
        }
        composable(NavigationRoute.Mood.route) {
            val moodRatingViewModel: MoodRatingViewModel = koinViewModel()
            MoodScreen(
                navController = navController,
                viewModel = moodRatingViewModel
            )
        }
        composable(NavigationRoute.Calendar.route) {
            val activityDialogViewModel: ActivityDialogViewModel = koinViewModel()
            CalendarScreen(viewModel = activityDialogViewModel)
        }
        composable(NavigationRoute.Exercises.route) {
            ExercisesScreen(
                navController = navController
            )
        }
        composable(NavigationRoute.Breathing.route) {
            BreathingScreen()
        }
        composable(NavigationRoute.Meditation.route) {
            MeditationScreen()
        }
        composable(NavigationRoute.Profile.route) {
            val profileViewModel: ProfileViewModel = koinViewModel()
            ProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel
            )
        }
        composable(NavigationRoute.Stats.route) {
            val chartViewModel: ChartViewModel = koinViewModel()
            ChartScreen(viewodel = chartViewModel)
        }
    }
}