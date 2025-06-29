package com.example.worklifebalance.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.worklifebalance.R
import com.example.worklifebalance.ui.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.worklifebalance.navigation.Screen

@Composable
fun Login(
    navController: NavController,
    context: Context = LocalContext.current
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val signInState by authViewModel.signInState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            coroutineScope.launch {
                isLoading = true
                authViewModel.signIn(credential)
            }
        } catch (e: Exception) {
            isLoading = false
            Toast.makeText(context, "Đăng nhập thất bại: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(signInState) {
        android.util.Log.d("LoginScreen", "LaunchedEffect(signInState) triggered: $signInState")
        val state = signInState
        if (state != null) {
            state.onSuccess {
                android.util.Log.d("LoginScreen", "Đăng nhập thành công! userId=${it.user?.uid}")
                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                val userId = it.user?.uid ?: return@onSuccess
                isLoading = false
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
                authViewModel.clearSignInState() // Đảm bảo reset state sau khi chuyển màn hình
            }
            state.onFailure {
                isLoading = false
                android.util.Log.e("LoginScreen", "Đăng nhập thất bại", it)
                Toast.makeText(context, "Đăng nhập thất bại: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                authViewModel.clearSignInState()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // App Logo
        Box(
            modifier = Modifier
                .padding(40.dp)
                .size(50.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(Color.White, RoundedCornerShape(12.dp))
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_removebg_preview),
                contentDescription = "App Name",
                modifier = Modifier
                    .padding(8.dp)
                    .size(40.dp),
                contentScale = ContentScale.Fit
            )
        }
        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 180.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Work-Life Balance",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Đăng nhập để đồng bộ mục tiêu, nhận thông báo quan trọng và " +
                        "cá nhân hóa trải nghiệm cân bằng giữa công việc và cuộc sống của bạn!",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 36.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            // Google Sign In Button
            Button(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.google_web_client_id))
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                },
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(24.dp))
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                enabled = !isLoading
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_color_svgrepo_com),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Đăng nhập bằng Google",
                        color = Color(0xFF5F6368),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
