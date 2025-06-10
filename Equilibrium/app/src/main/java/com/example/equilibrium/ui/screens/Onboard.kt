package com.example.equilibrium.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.equilibrium.R
import com.airbnb.lottie.compose.*

data class OnboardingPage(
    val title: String,
    val description: String,
    val lottieRes: Int
)

@Composable
fun Onboard(
    onSkip: () -> Unit,
    onDone: () -> Unit
) {
    val onboardingPage = listOf(
        OnboardingPage(
            title = "Equilibrium biến cuộc sống thành hành trình có định hướng.",
            description = "Không chỉ là việc làm xong danh sách to-do. Equilibrium giúp bạn xác định và theo đuổi mục tiêu dài hạn trong mọi khía cạnh cuộc sống.",
            lottieRes = R.drawable.onboard_equilibrium1
        ),
        OnboardingPage(
            title = "Lắng nghe năng lượng - Làm việc thông minh hơn.",
            description = "Bạn không phải lúc nào cũng tràn đầy năng lượng - điều đó hoàn toàn bình thường. Equilibrium gợi ý nhiệm vụ phù hợp theo mức năng lượng.",
            lottieRes = R.drawable.onboard_equilibrium2
        ),
        OnboardingPage(
            title = "Cân bằng là nền tảng để phát triển lâu dài.",
            description = "Tính năng \"Me Time\" giúp bạn chăm sóc chính mình giữa cuồng quay công việc - nghỉ ngơi, tái tạo, sống chậm lại.",
            lottieRes = R.drawable.onboard_equilibrium3
        )
    )

    var currentPage by rememberSaveable { mutableIntStateOf(0) }
    val isLastPage = currentPage == onboardingPage.lastIndex

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            OnboardingContent(page = onboardingPage[currentPage])

            DotIndicator(
                totalDots = onboardingPage.size,
                selectedIndex = currentPage
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentPage > 0) {
                    TextButton(onClick = { currentPage-- }) {
                        Text("Trở lại", color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    Spacer(modifier = Modifier.width(64.dp))
                }

                Button(
                    onClick = {
                        if (!isLastPage) currentPage++
                        else onDone()
                    }
                ) {
                    Text(if (isLastPage) "Bắt đầu" else "Tiếp tục")
                }
            }
        }

        TextButton(
            onClick = onSkip,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Bỏ qua", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun OnboardingContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(page.lottieRes))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever // Lặp lại vô hạn
        )

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .height(220.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = page.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@Composable
fun DotIndicator(totalDots: Int, selectedIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(4.dp)
                    .background(
                        color = if (index == selectedIndex) MaterialTheme.colorScheme.primary else Color.Gray,
                        shape = CircleShape
                    )
            )
        }
    }
}

