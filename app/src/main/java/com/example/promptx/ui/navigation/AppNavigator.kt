package com.example.promptx.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.promptx.ui.screen.LogInScreen
import com.example.promptx.ui.screen.OnboardingScreen
import com.example.promptx.ui.screen.PromptXMainScreen
import com.example.promptx.ui.screen.SignUpScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "onBoard") {
        composable("onBoard") { OnboardingScreen(navController) }
        composable("login") { LogInScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("generation") { PromptXMainScreen(navController) }
    }
}
