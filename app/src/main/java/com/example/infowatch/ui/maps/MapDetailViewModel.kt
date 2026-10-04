package com.example.infowatch.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infowatch.data.MapsRepository
import com.example.infowatch.model.Map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapDetailViewModel(
    private val repository: MapsRepository,
    private val mapKey: String,
) : ViewModel() {


    private val _mapDetail = MutableStateFlow<Map?>(null)
    val mapDetail: StateFlow<Map?> = _mapDetail

    init {
        loadMapDetail()
    }

    private fun loadMapDetail() {
        viewModelScope.launch {
            try {
                _mapDetail.value = repository.getMapByKey(mapKey)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}