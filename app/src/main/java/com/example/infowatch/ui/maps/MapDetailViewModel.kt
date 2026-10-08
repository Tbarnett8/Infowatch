package com.example.infowatch.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infowatch.domain.MapsRepository
import com.example.infowatch.model.Map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class MapDetailViewModel(
    private val repository: MapsRepository,
    private val mapKey: String,
) : ViewModel() {


    private val _uiState = MutableStateFlow<MapDetailUiState>(MapDetailUiState.Loading)
    val uiState: StateFlow<MapDetailUiState> = _uiState.asStateFlow()

    init {
        loadMapDetail()
    }

    fun loadMapDetail() {
        viewModelScope.launch {
            _uiState.value = MapDetailUiState.Loading

            try {
                val mapDetails = repository.getMapByKey(mapKey)
                _uiState.value = MapDetailUiState.Success(mapDetails)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = MapDetailUiState.Error(
                    message = "Unable to load map details"
                )
            }
        }
    }
}

sealed interface MapDetailUiState {
    data object Loading : MapDetailUiState
    data class Success(val mapDetails: Map?) : MapDetailUiState
    data class Error(val message: String) : MapDetailUiState
}