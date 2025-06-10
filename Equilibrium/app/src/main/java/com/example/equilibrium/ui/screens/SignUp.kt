

package com.example.equilibrium.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import com.example.equilibrium.R
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.equilibrium.ui.theme.EquilibriumTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignUpScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFE5DCF6),
                    Color(0xFFD1E5F5)
                )
            )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign Up",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "User Icon") },
            label = { Text("User Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email Icon") },
            label = { Text("Enter Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
            label = { Text("Create Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF9467EA),
                            Color(0xFF7CB9E7)
                        )
                    ),
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Text("Sign In", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Already have an account? Login")
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            IconButton(onClick = {}) {
                Icon(painterResource(id = R.drawable.google), contentDescription = "Google")
            }
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = {}) {
                Icon(painterResource(id = R.drawable.facebook), contentDescription = "Facebook")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text("Don’t have an account? Sign up")
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    EquilibriumTheme {
        SignUpScreen()
    }
}
