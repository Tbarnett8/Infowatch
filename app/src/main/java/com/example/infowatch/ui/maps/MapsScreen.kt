package com.example.infowatch.ui.maps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.infowatch.model.BackgroundImageSize
import com.example.infowatch.model.Map
import com.example.infowatch.model.MapGamemode
import com.example.infowatch.ui.heroes.RoleHeader
import com.example.infowatch.ui.heroes.backgroundUrlFor
import com.example.infowatch.ui.theme.Black
import com.example.infowatch.ui.theme.White
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapsScreen(
    onMapClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MapsViewModel = koinViewModel()
) {
    val maps by viewModel.maps.collectAsState()

    val grouped = maps.groupBy { it.gamemodes.first() }

    val modeUiOrder = listOf(
        MapGamemode.hybrid,
        MapGamemode.escort,
        MapGamemode.control,
        MapGamemode.push,
        MapGamemode.clash,
        MapGamemode.flashpoint,
        MapGamemode.deathmatch,
        MapGamemode.assault,
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        modeUiOrder.forEach { mode ->

            val mapsInMode = grouped[mode] ?: return@forEach

            if (mapsInMode.isNotEmpty()) {

                item {
                    RoleHeader(
                        role = mode.value,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                items(mapsInMode) { map ->
                    MapBlock(
                        map = map,
                        onClick = { onMapClick(map.key.name) }
                    )
                }
            }
        }
    }
}

@Composable
fun MapBlock(
    map: Map,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clickable { onClick() },
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = map.screenshot.toString(),
                contentDescription = map.name,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = White,
                            strokeWidth = 6.dp
                        )
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Black
                            )
                        )
                    )
            )
            Text(
                text = map.name,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
        }
    }
}