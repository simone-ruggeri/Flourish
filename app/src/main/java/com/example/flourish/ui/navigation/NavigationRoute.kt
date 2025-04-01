package com.example.flourish.ui.navigation

sealed class NavigationRoute (val route: String) {
    object Login: NavigationRoute("login")
    object SignUp: NavigationRoute ("signup")
    object Homepage: NavigationRoute ("homepage")
    object Sleep: NavigationRoute("sleep")
    object Mood: NavigationRoute("mood")
    object Calendar: NavigationRoute("calendar")
    object Exercises: NavigationRoute("exercises")
    object Breathing: NavigationRoute("breathing")
    object Meditation: NavigationRoute("meditation")
}