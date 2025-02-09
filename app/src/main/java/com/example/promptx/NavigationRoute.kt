package com.example.promptx

sealed class NavigationScreen(val route: String) {
    object GetStarted : NavigationScreen("getStarted")
    object LoginScreen : NavigationScreen("login")
    object SignInScreen : NavigationScreen("signIn")
}