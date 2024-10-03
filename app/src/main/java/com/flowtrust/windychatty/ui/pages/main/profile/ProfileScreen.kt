package com.flowtrust.windychatty.ui.pages.main.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.flowtrust.windychatty.domain.models.Avatar
import com.flowtrust.windychatty.domain.models.UserData
import com.flowtrust.windychatty.ui.common.CustomOutlinedTextField
import com.flowtrust.windychatty.ui.common.base64ToBitmap
import com.flowtrust.windychatty.ui.common.encodeUriToBase64
import com.flowtrust.windychatty.ui.theme.darkOlive
import com.flowtrust.windychatty.ui.theme.oliv
@Composable
fun ProfileScreen(
    userData: UserData,
    onBack: () -> Unit,
    onSave: (UserData, Avatar) -> Unit,
) {
    val context = LocalContext.current
    var isEditing by remember { mutableStateOf(false) }  // Флаг для режима редактирования

    val cityState = remember { mutableStateOf(userData.profile_data.city ?: "") }
    val statusState = remember { mutableStateOf(userData.profile_data.status ?: "") }
    val birthdayState = remember { mutableStateOf(userData.profile_data.birthday ?: "") }
    val nameState = remember { mutableStateOf(userData.profile_data.name) }
    val vkState = remember { mutableStateOf(userData.profile_data.vk ?: "") }
    val instagramState = remember { mutableStateOf(userData.profile_data.instagram ?: "") }
    val taskState = remember{ mutableStateOf(userData.profile_data.completed_task.toString())}


    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri  // Получаем URI выбранного файла
    }


    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { onBack.invoke() }
                    .padding(8.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            )
            Text(
                "Профиль",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data( if(selectedImageUri!=null) selectedImageUri
                    else  base64ToBitmap(userData.profile_data.avatar))
                    .allowConversionToBitmap(true)
                    .decoderFactory(SvgDecoder.Factory())
                    .crossfade(750)
                    .build(),
                contentScale = ContentScale.Crop
            ),
            contentDescription = "Аватар",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .sizeIn(128.dp, 128.dp, 164.dp, 164.dp)
                .padding(16.dp)
                .clip(CircleShape)
                .border(1.dp, darkOlive, CircleShape)
                .clickable {
                    if (isEditing) launcher.launch("image/*")
                }
        )
        if(isEditing){
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Нажмите на кружок, чтобы изменить фото", style = MaterialTheme.typography.bodySmall)
        }


        Spacer(modifier = Modifier.height(16.dp))

        if (isEditing) {
            EditableFields(label = "Имя", state = nameState)
            EditableFields(label = "Город", state = cityState)
            EditableFields(label = "Статус", state = statusState)
            EditableFields(label = "Дата рождения", state = birthdayState)
            EditableFields(label = "VK", state = vkState)
            EditableFields(label = "Instagram", state = instagramState)
            EditableFields(label = "Завершено задач", state = taskState)
        } else {
            // Отображение без редактирования
            Text(
                userData.profile_data.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))

            Info(title = "Имя пользователя:", data = userData.profile_data.username)
            Info(title = "Номер:", data = "+${userData.profile_data.phone}")
            Info(title = "Город:", data = userData.profile_data.city ?: "-")
            Info(title = "Статус:", data = userData.profile_data.status ?: "-")
            Info(title = "Дата рождения:", data = userData.profile_data.birthday ?: "-")
            Info(title = "VK:", data = userData.profile_data.vk ?: "-")
            Info(title = "Instagram:", data = userData.profile_data.instagram ?: "-")
            Info(title = "Онлайн:", data = if(userData.profile_data.online)"Да" else "Нет")
            Info(title = "Завершено задач:", data = userData.profile_data.completed_task.toString())
            Info(title = "Профиль создан:", data = userData.profile_data.created)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка редактирования/сохранения
        if (isEditing) {
          Button( colors = ButtonDefaults.buttonColors(containerColor = oliv,
                contentColor = Color.DarkGray,
                disabledContentColor = oliv,
                disabledContainerColor = Color.LightGray),
                onClick = {
                    val avatarBase64 = selectedImageUri?.let { encodeUriToBase64(context,it) } ?: userData.profile_data.avatar
                    val avatar = Avatar(avatarBase64, selectedImageUri?.authority ?: "avatar")  // Пример с новым аватаром

                    fun isValidDate(date: String): Boolean {
                        val regex = """\d{4}-\d{2}-\d{2}""".toRegex()
                        return regex.matches(date)
                    }

                    // Сохранение данных
                    val profileData = userData.profile_data.copy(
                        city = cityState.value,
                        status = statusState.value,
                        birthday = if(isValidDate(birthdayState.value)) birthdayState.value else userData.profile_data.birthday ,
                        name = nameState.value,
                        vk = vkState.value,
                        instagram = instagramState.value,
                        avatar = avatar.base_64,
                        completed_task = taskState.value.toInt()
                    )

                    onSave(
                        userData.copy(
                            profile_data = profileData
                        ),
                        avatar
                    )
                    isEditing = false
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Сохранить")
            }
        } else {
            Row(modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(oliv)
                .clickable { isEditing = true }
                .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "Изменить", style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.DarkGray
                ))
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать профиль",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp)
                )
                }
        }
    }
}

@Composable
fun Info(title:String, data:String){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom){
        Text(title, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.alpha(0.7f))
        Text(data,style = MaterialTheme.typography.bodyLarge)
    }
    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp))
}
@Composable
fun EditableFields(
    label: String,
    state: MutableState<String>
) {
    CustomOutlinedTextField(
        value = state.value,
        onValueChange = { state.value = it },
        label = label
    )
    Spacer(modifier = Modifier.height(8.dp))
}
