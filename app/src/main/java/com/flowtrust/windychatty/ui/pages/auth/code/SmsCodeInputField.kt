package com.flowtrust.windychatty.ui.pages.auth.code

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flowtrust.windychatty.ui.theme.black
import com.flowtrust.windychatty.ui.theme.blue

@Composable
fun SmsCodeInputField(
    smsCode: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val configuration = LocalConfiguration.current
    val isScreenSmall = configuration.screenWidthDp < 400

    var lastNewValue by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 0 until 6) {
            val isCurrentFieldEmpty = smsCode.length <= i

            TextField(
                modifier = Modifier
                    .padding(4.dp)
                    .size(
                        width = if (isScreenSmall) 40.dp else 50.dp,
                        height = if (isScreenSmall) 48.dp else 58.dp
                    )
                    .background(color = black, shape = MaterialTheme.shapes.small)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.key == Key.Backspace && keyEvent.type == KeyEventType.KeyUp) {
                            if (smsCode.isNotEmpty() && i > 0 && isCurrentFieldEmpty) {
                                focusManager.moveFocus(FocusDirection.Previous)
                                onValueChange(smsCode.dropLast(1))
                            }
                            true
                        } else {
                            false
                        }
                    },
                value = if (isCurrentFieldEmpty) "" else smsCode[i].toString(),
                onValueChange = { newValue ->
                    lastNewValue = newValue
                    when {
                        newValue.isNotEmpty() -> {
                            if (isCurrentFieldEmpty) {
                                onValueChange(smsCode + newValue)
                                if (i == 5 && smsCode.length == 5) {
                                    focusManager.clearFocus()
                                    onDone()
                                } else {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }
                            } else {
                                onValueChange(
                                    smsCode.replaceRange(i, i + 1, newValue.last().toString())
                                )
                            }
                        }
                        newValue.isEmpty() -> {
                            if (!isCurrentFieldEmpty) {
                                onValueChange(smsCode.removeRange(i, i + 1))
                            }
                        }
                    }
                },
                singleLine = true,
                maxLines = 1,
                textStyle = if (isScreenSmall)
                    MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center, fontSize = 16.sp)
                else
                    MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center, fontSize = 17.sp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onPrimary
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (i == 5) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                    onDone = {
                        focusManager.clearFocus()
                        if (smsCode.length == 6) onDone()
                    }
                )
            )
        }
    }
}
