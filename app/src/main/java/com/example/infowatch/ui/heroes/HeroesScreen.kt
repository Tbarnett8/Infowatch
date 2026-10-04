package com.example.infowatch.ui.heroes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.infowatch.model.HeroShort
import com.example.infowatch.model.Role
import org.koin.androidx.compose.koinViewModel

@Composable
fun HeroesScreen(
    onHeroClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HeroesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        HeroesUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }

        is HeroesUiState.Success -> {
            HeroGrid(
                heroes = state.heroes,
                onHeroClick = onHeroClick
            )
        }

        is HeroesUiState.Error -> {
            ErrorMessage(
                message = state.message,
                onRetry = viewModel::loadHeroes
            )
        }
    }
}

@Composable
fun HeroGrid(
    modifier: Modifier = Modifier,
    heroes: List<HeroShort>,
    onHeroClick: (String) -> Unit,
) {

    val grouped = heroes.groupBy { it.role }

    val roleUiOrder = listOf(
        Role.tank,
        Role.damage,
        Role.support
    )

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3),
    ) {

        roleUiOrder.forEach { role ->

            val heroesInRole = grouped[role] ?: return@forEach

            if (heroesInRole.isNotEmpty()) {

                item(span = { GridItemSpan(maxLineSpan) }) {
                    SectionHeader(
                        role = role.value,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                items(heroesInRole) { hero ->
                    HeroCard(
                        hero = hero,
                        onClick = { onHeroClick(hero.key.name) },
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    role: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = role.uppercase(),
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier.padding(top = 8.dp)
    )
}

@Composable
fun HeroCard(
    hero: HeroShort,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeroPortrait(
                modifier = Modifier.fillMaxWidth(),
                imageUrl = hero.portrait.toString(),
            )
            Text(
                modifier = Modifier.padding(2.dp),
                text = hero.name.uppercase(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message)

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Retry")
        }
    }
}

