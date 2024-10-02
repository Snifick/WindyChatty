package com.flowtrust.windychatty.ui.pages.main.chats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.flowtrust.windychatty.ui.NavigationRoutes
import com.flowtrust.windychatty.R
import com.flowtrust.windychatty.ui.theme.blue
import com.flowtrust.windychatty.ui.theme.oliv
import com.google.gson.Gson
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(navController: NavController) {
    // Фейковый список чатов
    val chatList = listOf(
        Chat("Jane Smith", "See you tomorrow", "10:15", 0),
        Chat("Bob Marley", "Cool, thanks!", "09:45", 0)
    )

    Column(modifier = Modifier.padding(8.dp)){
        Row(modifier = Modifier.padding(start = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically){
            Image(
                imageVector = Icons.Filled.Menu,
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { navController.navigate(NavigationRoutes.MainProfile.route) }
                    .padding(8.dp)


            )
            Text(
                "Чаты",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val gson = Gson()
            items(chatList) { chat ->
                ChatItem(chat) {
                    val chatJson = URLEncoder.encode(gson.toJson(chat), "UTF-8")
                    navController.navigate("${NavigationRoutes.MainDialog.route}/$chatJson")
                }
            }
        }
    }
}
@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = oliv, contentColor = Color.DarkGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.av_placeholder),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(chat.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(chat.lastMessage, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(chat.time, style = MaterialTheme.typography.bodySmall)
                if (chat.unreadCount > 0) {
                    BadgeBox(chat.unreadCount)
                }
            }
        }
    }
}

@Composable
fun BadgeBox(unreadCount: Int) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$unreadCount",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun DialogScreen(chat: Chat, goBack:()->Unit) {
    val messages = listOf(
        Message(chat.name,chat.lastMessage,true),
        Message("John Doe", "Hi there!", true),
        Message("You", "Hello!", false),
        Message("John Doe", "How are you?", true),
    )
    val messageText = remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(8.dp)){
        Row(modifier = Modifier.padding(start = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically){
            Image(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { goBack() }
                    .padding(8.dp)


            )
            Text(
                chat.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            reverseLayout = true
        ) {
            item {
                ChatInputField(
                messageText = messageText.value,
                onMessageChange = { messageText.value = it },
                onSendClick = { /* Отправить сообщение */ }
            ) }
            items(messages) { message ->
                MessageItem(message)
            }
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (message.isReceived) Arrangement.Start else Arrangement.End
    ) {
        Card(
            colors = CardColors(containerColor = if (message.isReceived) oliv else blue,
                contentColor = Color.DarkGray,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Gray),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                message.content,
                modifier = Modifier.padding(8.dp),
                color = if (message.isReceived) Color.Black else Color.White
            )
        }
    }
}

data class Message(val sender: String, val content: String, val isReceived: Boolean)

@Composable
fun ChatInputField(messageText: String, onMessageChange: (String) -> Unit, onSendClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val colors = MaterialTheme.colorScheme
        OutlinedTextField(
            value = messageText,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Введите сообщение...") },
            maxLines = 1,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = colors.primary,
                unfocusedIndicatorColor = Color.Gray,
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface.copy(alpha = 0.7f),
                focusedSupportingTextColor = colors.onSurface,
                unfocusedSupportingTextColor = colors.onSurface.copy(alpha = 0.7f),
                focusedLabelColor = colors.primary,
                unfocusedLabelColor = colors.onSurface.copy(alpha = 0.7f),
            ),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onSendClick) {
            Text("Отправить")
        }
    }
    HorizontalDivider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
}
