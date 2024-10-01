package com.flowtrust.windychatty.ui.pages.auth.contry_picker

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.flowtrust.windychatty.data.countryData.Country
import com.flowtrust.windychatty.ui.theme.blue

@Composable
fun CountryCodePicker(
    listCountry:List<Country>,
    onBack:(county:Country?)->Unit
) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)){

            Image(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onBack(null) })

            Text(text = "Выберите страну",
                style = MaterialTheme.typography.titleLarge)

        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)){
            items(listCountry)
            { country ->
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onBack(country)
                        }) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Spacer(modifier = Modifier.size(4.dp))
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(country.flag)
                                    .decoderFactory(SvgDecoder.Factory())
                                    .memoryCachePolicy(CachePolicy.ENABLED) // Кэширование в памяти
                                    .crossfade(true)
                                    .build(),
                                contentScale = ContentScale.Crop
                            ),
                            contentDescription = "Флаг",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier.size(44.dp,30.dp).clip(RoundedCornerShape(4.dp))
                        )

                        Spacer(modifier = Modifier.size(12.dp))

                        Text(text = country.name,
                        style = MaterialTheme.typography.bodyMedium)
                    }

                    Text(text = country.dialCode,
                        style = MaterialTheme.typography.bodyMedium.copy(color = blue))

                }
            }
        }
    }
    BackHandler {
        onBack(null)
    }

}
