package com.example.promptx.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.promptx.ui.screen.OnboardingScreen

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "getStarted") {
//        composable("login") { LoginScreen(navController) }
//        composable("signup") { SignupScreen(navController) }
        composable("getStarted") { OnboardingScreen(navController) }
    }
}
