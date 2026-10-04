package com.example.infowatch.ui.maps

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.infowatch.model.Map
import com.example.infowatch.ui.theme.White
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MapDetailScreen(
    mapKey: String,
    onBackClick: () -> Unit,
    viewModel: MapDetailViewModel = koinViewModel { parametersOf(mapKey) }
) {
    val map by viewModel.mapDetail.collectAsState()
    val scrollState = rememberScrollState()

    map?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 24.dp)
                .verticalScroll(scrollState),
        ) {
            Box {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 2f)
                        .padding(bottom = 16.dp),
                    model = it.screenshot.toString(),
                    contentDescription = it.name,
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
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.statusBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = White,
                        contentDescription = "Back"
                    )
                }
            }

            MapDetailContent(
                map = it,
            )
        }
    }
}

@Composable
fun MapDetailContent(
    map: Map,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = map.name,
            style = MaterialTheme.typography.displaySmall,
            color = White,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = map.location,
            style = MaterialTheme.typography.bodyLarge,
            color = White,
            textAlign = TextAlign.Center,
        )
        Text(
            text = map.gamemodes.first().name.uppercase(),
            style = MaterialTheme.typography.bodyLarge,
            color = White,
            textAlign = TextAlign.Center,
        )
        map.getMapImage()?.let {
            ZoomableImage(
                image = painterResource(it)
            )
        } ?: Text(
            modifier = Modifier.padding(16.dp),
            text = "Map not yet available",
            color = White,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun ZoomableImage(
    image: Painter,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(1f, 5f)

        // Calculate max allowed offset
        val maxX = (containerSize.width * (newScale - 1)) / 2f
        val maxY = (containerSize.height * (newScale - 1)) / 2f

        val newOffset = if (newScale > 1f) {
            offset + (offsetChange * newScale)
        } else {
            Offset.Zero
        }

        offset = Offset(
            x = newOffset.x.coerceIn(-maxX, maxX),
            y = newOffset.y.coerceIn(-maxY, maxY)
        )

        scale = newScale
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clipToBounds()
            .onSizeChanged { containerSize = it }
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            if (scale > 1f) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scale = 3f
                            }
                        }
                    )
                }
                .transformable(state)
        )
    }
}