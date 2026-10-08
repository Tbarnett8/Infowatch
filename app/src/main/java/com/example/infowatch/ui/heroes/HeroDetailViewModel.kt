package com.example.infowatch.ui.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.infowatch.domain.HeroesRepository
import com.example.infowatch.model.Hero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class HeroDetailViewModel(
    private val repository: HeroesRepository,
    private val heroKey: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HeroDetailsUiState>(HeroDetailsUiState.Loading)
    val uiState: StateFlow<HeroDetailsUiState> = _uiState.asStateFlow()

    init {
        loadHeroDetails()
    }

    fun loadHeroDetails() {
        viewModelScope.launch {
            _uiState.value = HeroDetailsUiState.Loading

            try {
                val hero = repository.getHeroDetails(heroKey)
                _uiState.value = HeroDetailsUiState.Success(hero)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = HeroDetailsUiState.Error(
                    message = "Unable to load hero data"
                )
            }
        }
    }
}

sealed interface HeroDetailsUiState {
    data object Loading : HeroDetailsUiState
    data class Success(val hero: Hero) : HeroDetailsUiState
    data class Error(val message: String) : HeroDetailsUiState
}