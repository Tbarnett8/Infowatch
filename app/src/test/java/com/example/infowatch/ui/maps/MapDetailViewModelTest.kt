package com.example.infowatch.ui.maps

import com.example.infowatch.domain.FakeMapsRepository
import com.example.infowatch.model.Map
import com.example.infowatch.model.MapGamemode
import com.example.infowatch.model.MapKey
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.URI

@OptIn(ExperimentalCoroutinesApi::class)
class MapDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val testMap = Map(
        key = MapKey.kingsMinusRow,
        name = "King's Row",
        screenshot = URI(""),
        gamemodes = listOf(MapGamemode.escort),
        location = "London, England",
        countryCode = "GB"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        val repository = FakeMapsRepository()

        val viewModel = MapDetailViewModel(
            repository = repository,
            mapKey = "kingsMinusRow"
        )

        assertEquals(
            MapDetailUiState.Loading,
            viewModel.uiState.value
        )
    }

    @Test
    fun `load map details emits success`() = runTest {
        val repository = FakeMapsRepository().apply {
            mapToReturn = testMap
        }

        val viewModel = MapDetailViewModel(
            repository = repository,
            mapKey = "kingsMinusRow"
        )

        advanceUntilIdle()

        assertEquals(
            MapDetailUiState.Success(testMap),
            viewModel.uiState.value
        )

        assertEquals(
            "kingsMinusRow",
            repository.lastMapKey
        )
    }

    @Test
    fun `load map details emits error when repository fails`() = runTest {
        val repository = FakeMapsRepository().apply {
            shouldThrow = true
        }

        val viewModel = MapDetailViewModel(
            repository = repository,
            mapKey = "kingsMinusRow"
        )

        advanceUntilIdle()

        assertEquals(
            MapDetailUiState.Error("Unable to load map details"),
            viewModel.uiState.value
        )
    }

    @Test
    fun `load map details emits success with null when map is not found`() = runTest {
        val repository = FakeMapsRepository().apply {
            mapToReturn = null
        }

        val viewModel = MapDetailViewModel(
            repository = repository,
            mapKey = "unknown"
        )

        advanceUntilIdle()

        assertEquals(
            MapDetailUiState.Success(null),
            viewModel.uiState.value
        )
    }
}