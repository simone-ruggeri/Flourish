package com.example.flourish.ui.navigation

sealed class NavigationRoute (val route: String) {
    object Login: NavigationRoute("login")
    object SignUp: NavigationRoute ("signup")
    object Homepage: NavigationRoute ("homepage")
}