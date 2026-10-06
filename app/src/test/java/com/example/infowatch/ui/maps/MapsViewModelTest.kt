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
class MapsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testMaps = listOf(
        Map(
            key = MapKey.kingsMinusRow,
            name = "King's Row",
            screenshot = URI(""),
            gamemodes = listOf(MapGamemode.escort),
            location = "London, England",
            countryCode = "GB"
        )
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

        val viewModel = MapsViewModel(repository)

        assertEquals(
            MapsUiState.Loading,
            viewModel.uiState.value
        )
    }

    @Test
    fun `load maps emits success`() = runTest {

        val repository = FakeMapsRepository().apply {
            mapsToReturn = testMaps
        }

        val viewModel = MapsViewModel(repository)

        advanceUntilIdle()

        assertEquals(
            MapsUiState.Success(testMaps),
            viewModel.uiState.value
        )
    }

    @Test
    fun `load maps emits error when repository fails`() = runTest {
        val repository = FakeMapsRepository().apply {
            shouldThrow = true
        }

        val viewModel = MapsViewModel(repository)

        advanceUntilIdle()

        assertEquals(
            MapsUiState.Error("Unable to load maps"),
            viewModel.uiState.value
        )
    }

    @Test
    fun `retry after error loads maps successfully`() = runTest {

        val repository = FakeMapsRepository().apply {
            shouldThrow = true
        }

        val viewModel = MapsViewModel(repository)

        // Initial request fails
        advanceUntilIdle()

        assertEquals(
            MapsUiState.Error("Unable to load maps"),
            viewModel.uiState.value
        )

        // Simulate repository recovering
        repository.shouldThrow = false
        repository.mapsToReturn = testMaps

        // Simulate pressing Retry
        viewModel.loadMaps()

        advanceUntilIdle()

        assertEquals(
            MapsUiState.Success(testMaps),
            viewModel.uiState.value
        )

        assertEquals(
            2,
            repository.getMapsCallCount
        )
    }
}