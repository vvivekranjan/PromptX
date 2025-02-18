package com.example.promptx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.promptx.ui.navigation.AppNavigator
import com.example.promptx.ui.theme.PromptXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PromptXTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AppNavigator()
                }
            }
        }
    }
}