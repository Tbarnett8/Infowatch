package com.example.infowatch.ui.heroes

import androidx.compose.ui.graphics.Color
import com.example.infowatch.model.BackgroundImageSize
import com.example.infowatch.model.Hero
import com.example.infowatch.model.HitPoints
import com.example.infowatch.ui.theme.Armour
import com.example.infowatch.ui.theme.Orange
import com.example.infowatch.ui.theme.Shield
import com.example.infowatch.ui.theme.White

fun HitPoints.blocks(): List<HealthBlockType> {
    val blocks = mutableListOf<HealthBlockType>()

    // Helper to add blocks, including partial blocks
    fun addBlocks(amount: Int, type: HealthBlockType) {
        var remaining = amount
        while (remaining > 0) {
            blocks.add(type)
            remaining -= 25
        }
    }

    addBlocks(health, HealthBlockType.HEALTH)
    addBlocks(shields, HealthBlockType.SHIELDS)
    addBlocks(armor, HealthBlockType.ARMOR)

    return blocks
}

enum class HealthBlockType { HEALTH, SHIELDS, ARMOR }

fun HealthBlockType.color(): Color = when(this) {
    HealthBlockType.HEALTH -> White
    HealthBlockType.SHIELDS -> Shield
    HealthBlockType.ARMOR -> Armour
}

fun Hero.backgroundUrlFor(size: BackgroundImageSize): String? {
    return backgrounds
        .firstOrNull { size in it.sizes }
        ?.url
        ?.toString()
}