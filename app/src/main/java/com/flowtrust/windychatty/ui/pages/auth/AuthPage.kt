package com.flowtrust.windychatty.ui.pages.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.flowtrust.windychatty.R
import com.flowtrust.windychatty.ui.pages.auth.contry_picker.CountryCodePicker
import com.flowtrust.windychatty.ui.pages.auth.contry_picker.PhoneVisualTransformation
import com.flowtrust.windychatty.ui.theme.black

@Composable
fun AuthPage(viewModel: AuthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(text = "Авторизация",
            style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.size(12.dp))
        Text(text = "Пожалуйста, введите ваш номер телефона для авторизации либо регистрации",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.selectedCountry?.name?:"",
            onValueChange = { _ ->
            },
            label = { Text("Страна", style = MaterialTheme.typography.bodyMedium) },
            shape = RoundedCornerShape(8.dp),
            trailingIcon = { Image(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "" )},
            leadingIcon = {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(uiState.imgLink)
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(800)
                            .build(),
                        contentScale = ContentScale.FillBounds
                    ),
                    contentDescription = "Флаг",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(42.dp,28.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable {
                            expanded = true
                        }
                )

            },
            placeholder = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    expanded = true
                },
            singleLine = true,
            enabled = false,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Gray,
                disabledTextColor = Color.Gray,
                disabledSupportingTextColor = Color.Gray,
                disabledLabelColor = Color.Gray
            ),
        )




        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                TextField(
                    value = uiState.code,
                    onValueChange = { newCode ->
                        if (newCode.length <= 4 && newCode.all { it.isDigit() }) {
                            viewModel.onCodeChanged(newCode)
                        }
                    },
                    visualTransformation = CodeVisualTransformation(),
                    label = null,
                    placeholder = { Text("Код", style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier
                        .weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    )
                )

                VerticalDivider(thickness = 1.dp, modifier = Modifier.height(24.dp))
                // Поле для ввода номера телефона
                TextField(
                    value = uiState.phone,
                    onValueChange = { newPhone ->
                        if (newPhone.length <= uiState.phoneMask.filter { it.isDigit() }.length && newPhone.all { it.isDigit() }) {
                            viewModel.onPhoneChanged(newPhone)
                        }
                    },
                    visualTransformation = PhoneVisualTransformation(
                        uiState.phoneMask,
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.onBackground),
                    label = null,
                    placeholder = { Text(uiState.phoneMask.drop(uiState.code.length+1), style = MaterialTheme.typography.bodyMedium) },
                    modifier = Modifier.weight(3.1f),  // Телефон занимает больше пространства
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Phone
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit phone button
        Button(
            onClick = { viewModel.submitPhone() },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            enabled = !uiState.isLoading
        ) {
            Text("Отправить код", style = MaterialTheme.typography.bodyMedium)
        }



        if (uiState.isCodeSent) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.code,
                onValueChange = { viewModel.onCodeChanged(it) },
                label = { Text("Подтверждение кода") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.submitCode() },
                modifier = Modifier.fillMaxWidth(),

                enabled = !uiState.isLoading
            ) {
                Text("Подтвердить код")
            }
        }

        if (uiState.errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = uiState.errorMessage, style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center)
        }
    }

    if(expanded) {
        CountryCodePicker(listCountry = uiState.listCountry) { country ->
            if(country!=null)
            viewModel.onChangeCountry(country)
            expanded = false
        }
    }

}
