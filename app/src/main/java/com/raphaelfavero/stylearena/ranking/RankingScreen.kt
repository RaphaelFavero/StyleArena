package com.raphaelfavero.stylearena.ranking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.raphaelfavero.stylearena.R
import com.raphaelfavero.stylearena.core.network.Style
import com.raphaelfavero.stylearena.core.ui.ErrorDialog
import com.raphaelfavero.stylearena.core.ui.OrbitLoadingAnimation
import kotlinx.coroutines.delay


@Composable
fun RankingScreen() {
    val viewModel: DailyWinnersViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getDailyWinners()
    }

    when {
        state.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OrbitLoadingAnimation(Modifier.size(128.dp))
            }
        }

        state.ranking != null -> {
            state.ranking?.let { ranking ->
                Column(
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(text = stringResource(id = R.string.today_winners), fontSize = 32.sp)

                    RankingPodium(ranking, Modifier.weight(1f))

                }
            }
        }

        state.error -> ErrorDialog { viewModel.getDailyWinners() }
    }
}


@Composable
fun RankingPodium(ranking: Triple<Style, Style, Style>, modifier: Modifier = Modifier) {
    var showFirst by remember { mutableStateOf(false) }
    var showSecond by remember { mutableStateOf(false) }
    var showThird by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showFirst = true
        delay(500)
        showSecond = true
        delay(500)
        showThird = true
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        AnimatedVisibility(
            modifier = Modifier.fillMaxHeight(0.5f),
            visible = showFirst,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
        ) {
            Box {
                AsyncImage(
                    model = ranking.first.url,
                    contentDescription = ranking.first.id,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clip(RoundedCornerShape(8.dp))
                )


                Icon(
                    painter = painterResource(id = R.drawable.gold),
                    tint = Color.Unspecified,
                    contentDescription = stringResource(id = R.string.gold_medal),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(56.dp)
                        .padding(8.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(0.5f),
                visible = showSecond,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
            ) {
                Box {
                    AsyncImage(
                        model = ranking.second.url,
                        contentScale = ContentScale.Crop,
                        contentDescription = ranking.second.id,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.silver),
                        tint = Color.Unspecified,
                        contentDescription = stringResource(id = R.string.silver_medal),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(56.dp)
                            .padding(8.dp)
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = showThird,
                enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
            ) {
                Box {
                    AsyncImage(
                        model = ranking.third.url,
                        contentScale = ContentScale.Crop,
                        contentDescription = ranking.third.id,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.bronze),
                        contentDescription = stringResource(id = R.string.bronze_medal),
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(56.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }

}
