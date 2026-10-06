package com.example.infowatch.domain

import com.example.infowatch.model.Hero
import com.example.infowatch.model.HeroGamemode
import com.example.infowatch.model.HeroKey
import com.example.infowatch.model.HeroShort
import com.example.infowatch.model.Role
import com.example.infowatch.model.SubRole
import java.net.URI

class FakeHeroesRepository : HeroesRepository {

    var heroesToReturn: List<HeroShort> = listOf(
        HeroShort(
            key = HeroKey.ana,
            name = "Ana",
            portrait = URI(""),
            role = Role.support,
            subrole = SubRole.sharpshooter,
            gamemodes = listOf(HeroGamemode.quickplay)
        )
    )

    var heroToReturn: Hero? = null
    var shouldThrow = false

    var getHeroesCallCount = 0
    var getHeroDetailsCallCount = 0
    var lastHeroKey: String? = null

    override suspend fun getHeroes(): List<HeroShort> {
        getHeroesCallCount++

        if (shouldThrow) {
            throw RuntimeException("Test error")
        }

        return heroesToReturn
    }

    override suspend fun getHeroDetails(key: String): Hero {
        getHeroDetailsCallCount++
        lastHeroKey = key

        if (shouldThrow) {
            throw RuntimeException("Test error")
        }

        return requireNotNull(heroToReturn)
    }
}