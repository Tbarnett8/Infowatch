package com.example.infowatch.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infowatch.data.MapsRepository
import com.example.infowatch.model.Map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: MapsRepository,
) : ViewModel() {

    private val _maps = MutableStateFlow<List<Map>>(emptyList())
    val maps: StateFlow<List<Map>> = _maps

    init {
        loadMaps()
    }

    fun loadMaps() {
        viewModelScope.launch {
            try {
                _maps.value = repository.getMaps()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}