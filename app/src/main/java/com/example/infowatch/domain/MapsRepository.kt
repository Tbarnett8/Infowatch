package com.example.infowatch.domain

import com.example.infowatch.model.Map

interface MapsRepository {

    suspend fun getMaps(): List<Map>

    suspend fun getMapByKey(key: String): Map?
}