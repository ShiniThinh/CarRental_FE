package com.example.carrental_fe.screen.user.userNotificationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carrental_fe.dialog.ErrorDialog
import com.example.carrental_fe.dialog.SuccessDialog
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import kotlinx.coroutines.flow.filter

@Composable
fun NotificationScreen(
    vm: NotificationViewModel,
) {
    val isSuccess = vm.isSuccess.collectAsState()
    val isFailed = vm.isFailed.collectAsState()

    if (isSuccess.value)
    {
        SuccessDialog("Successfully Delete All Notifications",
            onDismiss = {
                vm.dismissSuccessDialog()
            })
    }
    if (isFailed.value)
    {
        ErrorDialog("Failed to Delete All Notifications",
            onDismiss = {
                vm.dismissErrorDialog()
            })
    }
    LaunchedEffect(vm.hasEnteredScreen.collectAsState().value) {
        snapshotFlow { vm.hasEnteredScreen.value }
            .filter { it }
            .collect {
                vm.markAllAsRead()
            }
    }
    val notifications = vm.notifications.collectAsState().value
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(16.dp)) {
        TopTitle("Notifications")
        Spacer(modifier = Modifier.height(10.dp))
        Row(
                modifier = Modifier
                .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent notifications",
                fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                , fontSize = 18.sp)
            Surface(
                shape = CircleShape,
                color = Color(0xFFF5F5F5),
                modifier = Modifier.size(48.dp).clickable{
                    vm.deleteAllNotifications()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bin),
                    contentDescription = "Delete",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(12.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(notifications) { notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (!notification.isRead) 1.dp else 0.dp,
                            color = if (!notification.isRead) Color(0xFF0D6EFD) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (!notification.isRead) Color(0xFFB5D3FF) else Color(0xFFF7F7F9)
                    )
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = notification.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(notification.title, fontWeight = FontWeight.Bold)
                            Text(notification.message, maxLines = 3, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
            }
        }
    }
}