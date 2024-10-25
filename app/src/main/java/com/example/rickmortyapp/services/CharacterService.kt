package com.example.rickmortyapp.services

import com.example.rickmortyapp.models.Character
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterService {

    @GET("characters")
    suspend fun getCharacters(): List<Character>

    @GET("characters/{id}")
    suspend fun getCharactersById(@Path("id") id: Int): Character
}