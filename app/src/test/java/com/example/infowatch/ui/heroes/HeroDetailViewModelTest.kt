package com.example.infowatch.ui.heroes

import com.example.infowatch.domain.FakeHeroesRepository
import com.example.infowatch.model.Hero
import com.example.infowatch.model.HeroKey
import com.example.infowatch.model.PerksContainer
import com.example.infowatch.model.Role
import com.example.infowatch.model.Story
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

@OptIn(ExperimentalCoroutinesApi::class)
class HeroDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val expectedHero = Hero(
        name = "Ana",
        description = "A skilled support sniper.",
        backgrounds = emptyList(),
        role = Role.support,
        subrole = SubRole.sharpshooter,
        location = "Cairo, Egypt",
        age = 62,
        birthday = null,
        abilities = emptyList(),
        perks = PerksContainer(
            minor = emptyList(),
            major = emptyList()
        ),
        story = Story(
            summary = "Ana is a veteran support hero.",
            media = null,
            chapters = emptyList()
        ),
        portrait = null,
        hitpoints = null,
        stadiumPowers = null
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

        val viewModel = HeroDetailViewModel(
            repository = repository,
            heroKey = HeroKey.ana.value
        )

        assertEquals(
            HeroDetailsUiState.Loading,
            viewModel.uiState.value
        )
    }

    @Test
    fun `load hero details emits success`() = runTest {

        val repository = FakeHeroesRepository().apply {
            heroToReturn = expectedHero
        }

        val viewModel = HeroDetailViewModel(
            repository = repository,
            heroKey = HeroKey.ana.value
        )

        advanceUntilIdle()

        assertEquals(
            HeroDetailsUiState.Success(expectedHero),
            viewModel.uiState.value
        )
    }

    @Test
    fun `load hero details emits error when repository fails`() = runTest {
        val repository = FakeHeroesRepository().apply {
            shouldThrow = true
        }

        val viewModel = HeroDetailViewModel(
            repository = repository,
            heroKey = HeroKey.ana.value
        )

        advanceUntilIdle()

        assertEquals(
            HeroDetailsUiState.Error("Unable to load hero data"),
            viewModel.uiState.value
        )
    }

    @Test
    fun `retry after error loads hero successfully`() = runTest {

        val repository = FakeHeroesRepository().apply {
            shouldThrow = true
        }

        val viewModel = HeroDetailViewModel(
            repository = repository,
            heroKey = HeroKey.ana.value
        )

        // Initial request fails
        advanceUntilIdle()

        assertEquals(
            HeroDetailsUiState.Error("Unable to load hero data"),
            viewModel.uiState.value
        )

        // Repository works again
        repository.shouldThrow = false
        repository.heroToReturn = expectedHero

        // Simulate pressing Retry
        viewModel.loadHeroDetails()

        advanceUntilIdle()

        assertEquals(
            HeroDetailsUiState.Success(expectedHero),
            viewModel.uiState.value
        )

        assertEquals(
            2,
            repository.getHeroDetailsCallCount
        )
    }

    @Test
    fun `load hero details uses provided hero key`() = runTest {

        val repository = FakeHeroesRepository().apply {
            heroToReturn = expectedHero
        }

        val viewModel = HeroDetailViewModel(
            repository = repository,
            heroKey = HeroKey.ana.value
        )

        viewModel.loadHeroDetails()

        advanceUntilIdle()

        assertEquals(
            HeroKey.ana.value,
            repository.lastHeroKey
        )
    }
}