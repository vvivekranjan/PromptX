package com.example.promptx

sealed class NavigationScreen(val route: String) {
    object OnBoardScreen : NavigationScreen("onBoard")
    object LogInScreen : NavigationScreen("login")
    object SignUpScreen : NavigationScreen("signUp")
    object PromptGenerationScreen : NavigationScreen("generation")
}