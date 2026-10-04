package com.example.infowatch.ui.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infowatch.data.HeroesRepository
import com.example.infowatch.model.HeroShort
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeroesViewModel(
    private val repository: HeroesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HeroesUiState>(HeroesUiState.Loading)
    val uiState: StateFlow<HeroesUiState> = _uiState.asStateFlow()

    init {
        loadHeroes()
    }

    fun loadHeroes() {
        viewModelScope.launch {
            _uiState.value = HeroesUiState.Loading

            try {
                val heroes = repository.getHeroes()
                _uiState.value = HeroesUiState.Success(heroes)
            } catch (e: Exception) {
                _uiState.value = HeroesUiState.Error(
                    message = "Unable to load heroes"
                )
            }
        }
    }
}

sealed interface HeroesUiState {
    data object Loading : HeroesUiState
    data class Success(val heroes: List<HeroShort>) : HeroesUiState
    data class Error(val message: String) : HeroesUiState
}
