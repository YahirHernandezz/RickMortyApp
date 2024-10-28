package com.example.rickmortyapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import com.example.rickmortyapp.models.Character
import com.example.rickmortyapp.models.Location
import com.example.rickmortyapp.models.Origin
import com.example.rickmortyapp.services.CharacterService
import com.example.rickmortyapp.ui.templates.CharacterDetailTemplate
import com.example.rickmortyapp.ui.theme.RickMortyAppTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun CharacterDetailScreen(id:Int,innerPaddingValues: PaddingValues){
    val scope = rememberCoroutineScope()
    var isLoading by remember {
        mutableStateOf(false)
    }
    var character by remember {
        mutableStateOf(Character(
            id = 0,
            name = "",
            image = "",
            status = "",
            created = "",
            episode = emptyList(),
            gender = "",
            location = Location(name = "", url = ""),
            origin = Origin(name = "", url = ""),
            species = "",
            type = "",
            url = ""
        ))
    }

    val BASE_URL = "https://rickandmortyapi.com/api/"
    val productService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CharacterService::class.java)

    LaunchedEffect(key1 = true) {
        scope.launch {
            isLoading = true
            try {
                character = productService.getCharactersById(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isLoading = false
        }
    }
    if(isLoading){
        Box(
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator()
        }
    }
    else{
        val statusIcon = when (character.status) {
            "Alive" -> Icons.Default.Favorite
            "Dead" -> Icons.Default.Close
            else -> Icons.Default.Close
        }
        val statusColor = when (character.status) {
            "Alive" -> Color.Green
            "Dead" -> Color.Red
            else -> Color.Gray
        }

        //LLAMAMOS AL TEMPLATE
        CharacterDetailTemplate(
            image = character.image,
            name = character.name,
            statusIcon = statusIcon,
            statusColor = statusColor,
            species = character.species,
            gender = character.gender,
            location = character.location.name,
            origin = character.origin.name,
            created = character.created
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProductDetailScreenPreview(){
    RickMortyAppTheme {
        CharacterDetailScreen(id= 1,innerPaddingValues = PaddingValues(0.dp))
    }
}