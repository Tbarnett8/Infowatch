package com.example.infowatch.domain

import com.example.infowatch.model.Map

class FakeMapsRepository : MapsRepository {

    var mapsToReturn: List<Map> = emptyList()
    var mapToReturn: Map? = null

    var shouldThrow = false

    var getMapsCallCount = 0
    var getMapByKeyCallCount = 0
    var lastMapKey: String? = null

    override suspend fun getMaps(): List<Map> {
        getMapsCallCount++

        if (shouldThrow) {
            throw RuntimeException("Test error")
        }

        return mapsToReturn
    }

    override suspend fun getMapByKey(key: String): Map? {
        getMapByKeyCallCount++
        lastMapKey = key

        if (shouldThrow) {
            throw RuntimeException("Test error")
        }

        return mapToReturn
    }
}