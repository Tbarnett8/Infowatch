package com.example.infowatch.data

import com.example.infowatch.api.MapsApi
import com.example.infowatch.model.Map
import com.example.infowatch.model.MapKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MapsRepository(
    private val api: MapsApi
) {
    suspend fun getMaps(): List<Map> = withContext(Dispatchers.IO) {
        api.listMaps()
    }

    suspend fun getMapByKey(key: String): Map? = withContext(Dispatchers.IO) {
        api.listMaps().find { it.key == MapKey.valueOf(key) }
    }
}