package com.example.infowatch.ui.heroes

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.infowatch.model.Ability
import com.example.infowatch.model.BackgroundImageSize
import com.example.infowatch.model.Hero
import com.example.infowatch.model.HitPoints
import com.example.infowatch.model.Story
import com.example.infowatch.model.StoryChapter
import com.example.infowatch.ui.theme.Black
import com.example.infowatch.ui.theme.Blue
import com.example.infowatch.ui.theme.DarkBlue
import com.example.infowatch.ui.theme.Grey
import com.example.infowatch.ui.theme.White
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HeroDetailScreen(
    heroKey: String,
    onBackClick: () -> Unit,
    viewModel: HeroDetailViewModel = koinViewModel { parametersOf(heroKey) }
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    when (val state = uiState) {
        HeroDetailsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is HeroDetailsUiState.Success -> {
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
                        model = state.hero.backgroundUrlFor(BackgroundImageSize.lg),
                        contentDescription = state.hero.name,
                        contentScale = ContentScale.Crop,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
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

                HeroDetailContent(
                    hero = state.hero,
                )
            }
        }

        is HeroDetailsUiState.Error -> {
            ErrorMessage(
                message = state.message,
                onRetry = viewModel::loadHeroDetails
            )
        }
    }
}

@Composable
fun HeroDetailContent(
    hero: Hero,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = hero.name,
            style = MaterialTheme.typography.displaySmall,
            color = White,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = hero.description,
            style = MaterialTheme.typography.bodyLarge,
            color = White,
            textAlign = TextAlign.Center,
        )
        hero.hitpoints?.let { hp ->
            HealthBar(
                portraitUrl = hero.portrait.toString(),
                hitPoints = hp
            )
        }
        HeroAbilities(hero.abilities)
        HeroStory(hero.name, hero.story)
    }
}

@Composable
fun HealthBar(
    portraitUrl: String,
    hitPoints: HitPoints,
    modifier: Modifier = Modifier
) {
    val blocks = hitPoints.blocks()

    Column {
        Text(
            modifier = modifier.padding(vertical = 8.dp),
            text = "HEALTH",
            style = MaterialTheme.typography.titleSmall,
            color = White,
            fontWeight = FontWeight.Bold,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HeroPortrait(
                modifier = modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp)),
                imageUrl = portraitUrl,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .height(15.dp)
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    blocks.forEach { block ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    color = block.color(),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
                Text(
                    text = "Health: ${hitPoints.health}, Armour: ${hitPoints.armor}, Shield: ${hitPoints.shields}",
                    style = MaterialTheme.typography.titleMedium,
                    color = White,
                    fontWeight = FontWeight.Bold,
                )
            }

        }
    }
}

@Composable
fun HeroAbilities(
    abilities: List<Ability>,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            modifier = modifier.padding(vertical = 8.dp),
            text = "ABILITIES",
            style = MaterialTheme.typography.titleSmall,
            color = White,
            fontWeight = FontWeight.Bold,
        )
        abilities.forEach {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = modifier.height(40.dp),
                    model = it.icon.toString(),
                    contentDescription = ""
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = White,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = White,
                    )
                }
            }
        }
    }
}

@Composable
fun HeroStory(
    name: String,
    story: Story,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "STORY",
            style = MaterialTheme.typography.titleSmall,
            color = White,
            fontWeight = FontWeight.Bold,
        )
        if (story.summary.isNotEmpty()) {
            Column(
                modifier = modifier
                    .background(
                        color = White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .border(
                        BorderStroke(1.dp, White),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = "About $name",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = White,
                    textAlign = TextAlign.Justify,
                )
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = story.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = White,
                )
            }
        }
        var expandedIndex by remember { mutableStateOf<Int?>(null) }

        story.chapters.forEachIndexed { index, chapter ->
            StoryChapter(
                chapter = chapter,
                expanded = expandedIndex == index,
                onClick = {
                    expandedIndex =
                        if (expandedIndex == index) null else index
                }
            )
        }
    }
}

@Composable
fun StoryChapter(
    chapter: StoryChapter,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .animateContentSize()
            .background(
                color = White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                BorderStroke(1.dp, White),
                RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            text = chapter.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = White,
            textAlign = TextAlign.Justify,
        )

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = chapter.content,
            style = MaterialTheme.typography.bodyMedium,
            color = White,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
        )

        if (expanded) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(4.dp)),
                model = chapter.picture.toString(),
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = if (expanded) "Show less" else "Read more",
            color = White,
            style = MaterialTheme.typography.labelMedium
        )
    }
}