package com.flowtrust.windychatty.ui.pages.auth.code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flowtrust.windychatty.R

@Composable
fun AuthCodePage(
    viewModel: AuthCodeViewModel,
    phone:String,
    code:String,
    goForward: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var verifyCode by remember { mutableStateOf("")}
    val uiState by viewModel.uiState.collectAsState()
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                text = stringResource(R.string.enter_sms_code),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(R.string.on_number) + " +$code$phone "
                        + stringResource(id = R.string.was_send_sms_code),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            SmsCodeInputField(
                smsCode = verifyCode,
                onValueChange = { verifyCode = it },
                onDone = {
                    viewModel.submitCode(
                        verifyCode, code, phone,
                        goForward = goForward,
                        goRegister = onNavigateToRegister
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Button(onClick = {
                viewModel.submitCode(
                    verifyCode, code, phone,
                    goForward = { goForward.invoke() },
                    goRegister = { onNavigateToRegister.invoke() })},
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Text(
                        "Подтвердить код",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            Spacer(modifier = Modifier.height(8.dp))
            if (uiState.errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = uiState.errorMessage, style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center)
            }
            }



    }
}