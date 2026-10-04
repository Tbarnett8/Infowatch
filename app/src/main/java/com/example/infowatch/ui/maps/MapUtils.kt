package com.example.infowatch.ui.maps

import com.example.infowatch.R
import com.example.infowatch.model.Map
import com.example.infowatch.model.MapKey

fun Map.getMapImage() = when(this.key) {
    MapKey.kingsMinusRow -> R.drawable.kingsrow
    MapKey.eichenwalde -> R.drawable.eichenwalde
    MapKey.routeMinus66 -> R.drawable.route66
    MapKey.rialto -> R.drawable.rialto
    MapKey.blizzardMinusWorld -> R.drawable.blizzardworld
    MapKey.busan -> R.drawable.busan
    MapKey.numbani -> R.drawable.numbani
    MapKey.nepal -> R.drawable.nepal
    MapKey.hollywood -> R.drawable.hollywood
    MapKey.havana -> R.drawable.havana
    MapKey.oasis -> R.drawable.oasis
    MapKey.dorado -> R.drawable.dorado
    MapKey.lijiangMinusTower -> R.drawable.lijiangtower
    MapKey.ilios -> R.drawable.ilios
    MapKey.junkertown -> R.drawable.junkertown
    else -> null
}