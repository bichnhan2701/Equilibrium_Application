package com.example.worklifebalance.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.worklifebalance.R
import com.example.worklifebalance.ui.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.worklifebalance.navigation.Screen

@Composable
fun UserProfile(
    navController: NavController,
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val signOutState by authViewModel.signOutState.collectAsState()
    val context = LocalContext.current

    val user = FirebaseAuth.getInstance().currentUser
    val userPhotoUrl = user?.photoUrl
    val userName = user?.displayName ?: "Chưa đăng nhập"
    val userEmail = user?.email ?: ""

    val showSignOutDialog = remember { mutableStateOf(false) }

    LaunchedEffect(signOutState) {
        signOutState?.onSuccess {
            Toast.makeText(context, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show()
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            authViewModel.clearSignOutState()
        }
        signOutState?.onFailure {
            Toast.makeText(context, "Đăng xuất thất bại: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            authViewModel.clearSignOutState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(30.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8F8F8))
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF26948A),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                    Text(
                        text = "Thông tin cá nhân",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (userPhotoUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(userPhotoUrl),
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.logo_removebg_preview),
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .align(Alignment.Center)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userEmail,
                    color = Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        if (user != null) {
                            showSignOutDialog.value = true
                        } else {
                            Toast.makeText(context, "Vui lòng đăng nhập Google từ màn hình đăng nhập!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = if (user != null) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    else ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    if (user != null) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sign Out",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Đăng xuất", color = Color.White, fontSize = 16.sp)
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.google_color_svgrepo_com),
                            contentDescription = "Sign In With Google",
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = "Đăng nhập với Google", color = Color.Black, fontSize = 16.sp)
                    }

                }
                if (showSignOutDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showSignOutDialog.value = false },
                        title = { Text("Xác nhận đăng xuất") },
                        text = { Text("Bạn có chắc chắn muốn đăng xuất? Tất cả liệu của bạn sẽ bị mất.") },
                        confirmButton = {
                            TextButton(onClick = {
                                showSignOutDialog.value = false
                                authViewModel.signOut()
                            }) {
                                Text("Đăng xuất", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showSignOutDialog.value = false }) {
                                Text("Hủy")
                            }
                        }
                    )
                }
            }
        }
    }
}
