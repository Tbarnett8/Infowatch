package com.example.infowatch.ui.heroes

import com.example.infowatch.domain.FakeHeroesRepository
import com.example.infowatch.model.HeroGamemode
import com.example.infowatch.model.HeroKey
import com.example.infowatch.model.HeroShort
import com.example.infowatch.model.Role
import com.example.infowatch.model.SubRole
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
class HeroesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    val testHeroes = listOf(
        HeroShort(
            key = HeroKey.ana,
            name = "Ana",
            portrait = URI(""),
            role = Role.support,
            subrole = SubRole.sharpshooter,
            gamemodes = listOf(HeroGamemode.quickplay)
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

        val repository = FakeHeroesRepository()

        val viewModel = HeroesViewModel(repository)

        assertEquals(
            HeroesUiState.Loading,
            viewModel.uiState.value
        )
    }

    @Test
    fun `load heroes emits success`() = runTest {


        val repository = FakeHeroesRepository().apply {
            heroesToReturn = testHeroes
        }

        val viewModel = HeroesViewModel(repository)

        advanceUntilIdle()

        assertEquals(
            HeroesUiState.Success(testHeroes),
            viewModel.uiState.value
        )
    }

    @Test
    fun `load heroes emits error when repository fails`() = runTest {
        val repository = FakeHeroesRepository().apply {
            shouldThrow = true
        }

        val viewModel = HeroesViewModel(repository)

        advanceUntilIdle()

        assertEquals(
            HeroesUiState.Error("Unable to load heroes"),
            viewModel.uiState.value
        )
    }

    @Test
    fun `retry after error loads heroes successfully`() = runTest {

        val repository = FakeHeroesRepository().apply {
            shouldThrow = true
        }

        val viewModel = HeroesViewModel(repository)

        // Initial request fails
        advanceUntilIdle()

        assertEquals(
            HeroesUiState.Error("Unable to load heroes"),
            viewModel.uiState.value
        )

        // Simulate the repository working again
        repository.shouldThrow = false
        repository.heroesToReturn = testHeroes

        // Simulate the user pressing Retry
        viewModel.loadHeroes()

        advanceUntilIdle()

        assertEquals(
            HeroesUiState.Success(testHeroes),
            viewModel.uiState.value
        )

        assertEquals(2, repository.getHeroesCallCount)
    }
}