package com.example.infowatch.data

import com.example.infowatch.api.HeroesApi
import com.example.infowatch.model.Hero
import com.example.infowatch.model.HeroKey
import com.example.infowatch.model.HeroShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeroesRepository(
    private val api: HeroesApi
) {
    suspend fun getHeroes(): List<HeroShort> = withContext(Dispatchers.IO) {
        api.listHeroes()
    }

    suspend fun getHeroDetails(key: String): Hero = withContext(Dispatchers.IO) {
        api.getHero(HeroKey.valueOf(key))
    }
}