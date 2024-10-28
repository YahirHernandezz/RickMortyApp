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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickmortyapp.models.Character
import com.example.rickmortyapp.models.Location
import com.example.rickmortyapp.models.Origin
import com.example.rickmortyapp.services.CharacterService
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
        Column (
            modifier = Modifier
                .padding(innerPaddingValues)
                .fillMaxSize()
        ){
            AsyncImage(
                model = character.image,
                contentDescription = "${character.name}'s image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = character.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = "Status: ${character.status}", fontSize = 16.sp)
            Text(text = "Species: ${character.species}", fontSize = 16.sp)
            Text(text = "Gender: ${character.gender}", fontSize = 16.sp)
            Text(text = "Location: ${character.location.name}", fontSize = 16.sp)
            Text(text = "Origin: ${character.origin.name}", fontSize = 16.sp)
            Text(text = "Type: ${character.type}", fontSize = 16.sp)
            Text(text = "Episodes: ${character.episode.size}", fontSize = 16.sp)
            Text(text = "Created: ${character.created}", fontSize = 16.sp)
        }
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