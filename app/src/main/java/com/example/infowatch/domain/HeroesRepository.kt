package com.example.infowatch.domain

import com.example.infowatch.model.Hero
import com.example.infowatch.model.HeroShort

interface HeroesRepository {

    suspend fun getHeroes(): List<HeroShort>

    suspend fun getHeroDetails(key: String): Hero
}