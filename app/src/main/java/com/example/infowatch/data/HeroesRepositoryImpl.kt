package com.example.infowatch.data

import com.example.infowatch.api.HeroesApi
import com.example.infowatch.domain.HeroesRepository
import com.example.infowatch.model.Hero
import com.example.infowatch.model.HeroKey
import com.example.infowatch.model.HeroShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeroesRepositoryImpl(
    private val api: HeroesApi
) : HeroesRepository {

    override suspend fun getHeroes(): List<HeroShort> = withContext(Dispatchers.IO) {
        api.listHeroes()
    }

    override suspend fun getHeroDetails(key: String): Hero = withContext(Dispatchers.IO) {
        api.getHero(HeroKey.valueOf(key))
    }
}