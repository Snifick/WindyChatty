package com.flowtrust.windychatty.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flowtrust.windychatty.R
import com.flowtrust.windychatty.ui.theme.oliv
import com.flowtrust.windychatty.ui.theme.whiteRed

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    description: String,
    onButtonClick: () -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(
                    id = R.drawable.error_icon
                ),
                contentDescription = "",
                modifier = Modifier
                    .size(80.dp)
                    .background(color = oliv, shape = CircleShape)
                    .padding(20.dp)
            )
            Text(
                text ="Произошла ошибка",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.size(32.dp))
            MainButton(
                    text =  "Хорошо",
            onClick = { onButtonClick() },
            containerColor = oliv,
            textColor = Color.DarkGray,
            modifier = Modifier
                .padding(bottom = 16.dp, start = 32.dp, end = 32.dp)
                .fillMaxWidth()
            )
        }

    }
}