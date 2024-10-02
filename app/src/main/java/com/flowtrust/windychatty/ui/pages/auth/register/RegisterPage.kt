package com.flowtrust.windychatty.ui.pages.auth.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flowtrust.windychatty.ui.pages.auth.AuthViewModel
import com.flowtrust.windychatty.ui.theme.red

@Composable
fun RegisterPage(
    phone: String, // Номер телефона передаётся в этот экран
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToAuthPhone: () -> Unit,
    onNavigateToMainMenu:()->Unit
) {
    val context = LocalContext.current
    val name = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val isLoading = viewModel.isLoading.collectAsState()
    val isUsernameValid by viewModel.isUsernameValid.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val colors = MaterialTheme.colorScheme

        Text(
            text = "Регистрация",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = phone,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Поле для ввода имени пользователя
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Имя пользователя") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
            shape = RoundedCornerShape(8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Поле для ввода username
        OutlinedTextField(
            value = username.value,
            onValueChange = {
                username.value = it
                viewModel.onUsernameChange(it) }, // Используем ViewModel для валидации
            label = { Text("Username") },
            singleLine = true,
            isError = !isUsernameValid, // Показ ошибки валидации
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                errorIndicatorColor = red,
                errorCursorColor = red,
                errorLabelColor = red
            ),
            shape = RoundedCornerShape(8.dp),
        )

        if (!isUsernameValid) {
            Text(
                text = "Username может содержать только латинские буквы, цифры, - и _",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка отправки данных
        Button(
            onClick = {
                viewModel.registerUser(phone, name.value, username.value, onSuccess = {
                    Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                    onNavigateToMainMenu()
                }, onError = {
                    Toast.makeText(context, "Ошибка регистрации: $it", Toast.LENGTH_SHORT).show()
                })
            },
            enabled = viewModel.isFormValid(name.value, username.value),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = if (isLoading.value) "Загрузка..." else "Зарегистрироваться",
                style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка возврата
        Button(onClick = { onNavigateToAuthPhone() }) {
            Text("Назад",  style = MaterialTheme.typography.bodyMedium)
        }
    }
}
