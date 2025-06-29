package com.example.worklifebalance.ui.component.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.example.worklifebalance.R

@Composable
fun DashboardHeader(
    onViewProfile: () -> Unit = {},
    onViewRest: () -> Unit = {},
) {
    val userPhotoUrl = FirebaseAuth.getInstance().currentUser?.photoUrl?.toString()

    Surface(
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 4.dp,
    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(2f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_removebg_preview),
                        contentDescription = "Logo",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Work-Life Balance",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Text(
                    text = "Cân bằng cuộc sống của bạn!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onViewRest),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.healthicons_life_science),
                        contentDescription = "Xem hoạt động thư giãn",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable( onClick = onViewProfile ),
                    contentAlignment = Alignment.Center
                ) {
                    if (userPhotoUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(userPhotoUrl),
                            contentDescription = "User Avatar",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.avatar_nologin),
                            contentDescription = "Logo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}