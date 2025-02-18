package com.example.promptx.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1C1C1C), Color(0xFF3A3A3A))
    )

    val onboardingPageData = listOf(
        "PromptX" to "AI-Powered Prompt Generation",
        "Smart AI" to "Get Precise Prompts Instantly",
        "Save & Share" to "Keep Your Prompts Organized",
        "Get Started" to "Start Your AI Journey Today"
    )

    val onboardingPagerState = rememberPagerState(pageCount = { onboardingPageData.size + 2 }, initialPage = 1)
    val scope = rememberCoroutineScope()
    var isUserDragging by remember { mutableStateOf(false) }

    // Auto-scroll effect with stop on user interaction
    LaunchedEffect(onboardingPagerState.currentPage, isUserDragging) {
        while (!isUserDragging) {
            delay(3000) // Change slide every 3 seconds
            scope.launch {
                val nextPage = (onboardingPagerState.currentPage + 1) % onboardingPageData.size
                onboardingPagerState.animateScrollToPage(nextPage)
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { isUserDragging = true },
                    onDragEnd = { isUserDragging = false },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            if (dragAmount < -20) {
                                if (onboardingPagerState.currentPage == onboardingPageData.size + 1) {
                                    onboardingPagerState.scrollToPage(1) // Go to first actual page
                                } else {
                                    onboardingPagerState.animateScrollToPage(onboardingPagerState.currentPage + 1)
                                }
                            } else if (dragAmount > 20) {
                                if (onboardingPagerState.currentPage == 0) {
                                    onboardingPagerState.scrollToPage(onboardingPageData.size) // Go to last actual page
                                } else {
                                    onboardingPagerState.animateScrollToPage(onboardingPagerState.currentPage - 1)
                                }
                            }
                        }
                    }
                )
            }
    ) {
        val screenMaxHeight = maxHeight
        val screenMaxWidth = maxWidth

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(screenMaxHeight * 0.1f))

            // Animated Title & Subtitle (Perfectly Centered)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    state = onboardingPagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    val currentPageIndex = when (page) {
                        0 -> onboardingPageData.size - 1
                        onboardingPageData.size + 1 -> 0
                        else -> page - 1
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = onboardingPageData[currentPageIndex].first,
                            style = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = onboardingPageData[currentPageIndex].second,
                            style = TextStyle(fontSize = 16.sp, color = Color.White),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Dot Indicator (Sync with Titles)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(onboardingPageData.size) { index ->
                    val dotIndicatorSize by animateFloatAsState(
                        targetValue = if (index == (onboardingPagerState.currentPage - 1 + onboardingPageData.size) % onboardingPageData.size) 12f else 8f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    Box(
                        modifier = Modifier
                            .size(dotIndicatorSize.dp)
                            .background(
                                if (index == (onboardingPagerState.currentPage - 1 + onboardingPageData.size) % onboardingPageData.size) Color.White else Color.LightGray,
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("signup") },
                modifier = Modifier
                    .width(screenMaxWidth * 0.85f)
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(text = "GET STARTED", color = Color(0xFF1C1C1C), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("login") }) {
                Text(
                    text = "I ALREADY HAVE AN ACCOUNT",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GetStartedPreview() {
    OnboardingScreen(rememberNavController())
}
