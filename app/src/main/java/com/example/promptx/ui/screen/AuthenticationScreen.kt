package com.example.promptx.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.promptx.R

@Composable
fun LogInScreen(
    navController: NavController
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1C1C1C), Color(0xFF3A3A3A))
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visibilityEnable by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            // Back Arrow
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 40.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Back",
//                    tint = Color.White,
//                    modifier = Modifier.size(24.dp)
//                )
//            }

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = Color.White
                    )
                },
                placeholder = { Text("Email", color = Color.White.copy(0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
//                colors = TextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    textColor = Color.White
//                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon",
                        tint = Color.White
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { visibilityEnable = !visibilityEnable }) {
                        Icon(
                            painter = if (!visibilityEnable) painterResource(R.drawable.ic_visibility_on) else painterResource(R.drawable.ic_visibility_off),
                            contentDescription = "Visual Transformation",
                            tint = Color.White
                        )
                    }
                },
                visualTransformation = if (!visibilityEnable) PasswordVisualTransformation('*') else VisualTransformation.None,
                placeholder = { Text("Password", color = Color.White.copy(0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    unfocusedBorderColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    textColor = Color.White
//                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { navController.navigate("generation") },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50.dp),
                enabled = email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text("LOGIN", color = Color(0xFF1C1C1C), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Forgot Password
            Text(
                "Forgot Password?",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.clickable {}
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sign Up Section
            Row {
                Text("Don't have an account?", color = Color.White.copy(0.7f), fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Sign Up",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("signup") }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // OR Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.White.copy(0.5f)
                )
                Text(
                    "  OR  ",
                    color = Color.White.copy(0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.White.copy(0.5f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Social Media Icons
            Row(horizontalArrangement = Arrangement.Center) {
                SocialMediaIcon(painterResource(id = R.drawable.ic_facebook))
                Spacer(modifier = Modifier.width(18.dp))
                SocialMediaIcon(painterResource(id = R.drawable.ic_google))
                Spacer(modifier = Modifier.width(18.dp))
                SocialMediaIcon(painterResource(id = R.drawable.ic_twitter))
            }
        }
    }
}

@Composable
fun SignUpScreen(
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF1C1C1C), Color(0xFF3A3A3A))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            // Back Arrow
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 40.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Back",
//                    tint = Color.White,
//                    modifier = Modifier.size(24.dp)
//                )
//            }

            // Username Field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User Account",
                        tint = Color.White
                    )
                },
                placeholder = { Text("Username", color = Color.White.copy(0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
//                colors = TextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    textColor = Color.White
//                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = Color.White
                    )
                },
                placeholder = { Text("Email", color = Color.White.copy(0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
//                colors = TextFieldDefaults.colors(
//                    unfocusedBorderColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    textColor = Color.White
//                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon",
                        tint = Color.White
                    )
                },
                visualTransformation = PasswordVisualTransformation('*'),
                placeholder = { Text("Password", color = Color.White.copy(0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    unfocusedBorderColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    textColor = Color.White
//                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password Icon",
                        tint = Color.White
                    )
                },
                visualTransformation = PasswordVisualTransformation('*'),
                placeholder = { Text("Confirm Password", color = Color.White.copy(0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    unfocusedBorderColor = Color.White,
//                    focusedBorderColor = Color.White,
//                    textColor = Color.White
//                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(50.dp),
                enabled = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && (password == confirmPassword)
            ) {
                Text("SIGN UP", color = Color(0xFF1C1C1C), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sign In Section
            Row {
                Text("Already have an account?", color = Color.White.copy(0.7f), fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Sign In",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // OR Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.White.copy(0.5f)
                )
                Text(
                    "  OR  ",
                    color = Color.White.copy(0.7f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp),
                    color = Color.White.copy(0.5f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Social Media Icons
            Row(horizontalArrangement = Arrangement.Center) {
                SocialMediaIcon(painterResource(id = R.drawable.ic_facebook))
                Spacer(modifier = Modifier.width(18.dp))
                SocialMediaIcon(painterResource(id = R.drawable.ic_google))
                Spacer(modifier = Modifier.width(18.dp))
                SocialMediaIcon(painterResource(id = R.drawable.ic_twitter))
            }
        }
    }
}


@Composable
fun SocialMediaIcon(
    painter: Painter
) {
    Image(
        painter = painter,
        contentScale = ContentScale.Crop,
        contentDescription = "Social Media Icon",
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.Transparent)
            .clickable { }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LogInScreen(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUpScreen() {
    SignUpScreen(rememberNavController())
}
