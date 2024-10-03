package com.flowtrust.windychatty.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MainButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    containerColor: Color = Color.Transparent,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceTint,
    disabledTextColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    enabled: Boolean = true
) {
    val buttonColor = animateColorAsState(
        targetValue =
        if (enabled)
            containerColor
        else
            disabledContainerColor,
        label = "Button color"
    )
    val buttonTextColor = animateColorAsState(
        targetValue =
        if (enabled)
            textColor
        else
            disabledTextColor,
        label = "Button color"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = buttonColor.value,
                shape = MaterialTheme.shapes.extraLarge
            )
            .clip(MaterialTheme.shapes.extraLarge)
            .clickable(enabled = enabled) { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = buttonTextColor.value,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 32.dp)
        )
    }
}