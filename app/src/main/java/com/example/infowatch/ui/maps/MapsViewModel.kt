package com.example.infowatch.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infowatch.domain.MapsRepository
import com.example.infowatch.model.Map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: MapsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapsUiState>(MapsUiState.Loading)
    val uiState: StateFlow<MapsUiState> = _uiState.asStateFlow()

    init {
        loadMaps()
    }

    fun loadMaps() {
        viewModelScope.launch {
            _uiState.value = MapsUiState.Loading

            try {
                val maps = repository.getMaps()
                _uiState.value = MapsUiState.Success(maps)
            } catch (e: Exception) {
                _uiState.value = MapsUiState.Error(
                    message = "Unable to load maps"
                )
            }
        }
    }
}

sealed interface MapsUiState {
    data object Loading : MapsUiState
    data class Success(val maps: List<Map>) : MapsUiState
    data class Error(val message: String) : MapsUiState
}