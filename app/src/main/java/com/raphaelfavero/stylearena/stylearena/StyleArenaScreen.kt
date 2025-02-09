package com.raphaelfavero.stylearena.stylearena

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
fun StyleArenaScreen(onVoteCompleted: () -> Unit) {
    val viewModel: StyleArenaViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var selectedStyleId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getStylePair()
    }

    when {
        state.hasVoted -> {
            LaunchedEffect(Unit) {
                onVoteCompleted()
            }
        }

        state.isLoading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OrbitLoadingAnimation(Modifier.size(128.dp))
            }
        }

        state.stylePair != null -> {
            state.stylePair?.let { stylePair ->
                var showText by remember { mutableStateOf(false) }
                var showButton by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    delay(1000)
                    showText = true
                    showButton = true
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AnimatedVisibility(
                        visible = showText,
                        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                    ) {
                        Text(
                            text = stringResource(id = R.string.choose_favorite_style),
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }

                    StyleSelector(selectedStyleId, stylePair) { styleId ->
                        selectedStyleId = styleId
                    }

                    AnimatedVisibility(
                        visible = showButton,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    ) {
                        Button(
                            enabled = selectedStyleId != null,
                            onClick = {
                                viewModel.vote(selectedStyleId!!)
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        {
                            Text(text = stringResource(id = R.string.vote))
                        }
                    }
                }

                if (state.error) {
                    ErrorDialog(onDismiss = { viewModel.clearError() })
                }
            }
        }

        state.error -> {
            ErrorDialog(onDismiss = { viewModel.clearError() })
        }
    }
}

@Composable
fun StyleSelector(
    selectedStyleId: String?,
    stylePair: Pair<Style, Style>,
    onSelected: (String) -> Unit
) {
    var showSecond by remember { mutableStateOf(false) }
    var showFirst by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showFirst = true
        delay(500)
        showSecond = true
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(0.5f),
            visible = showFirst,
            enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
            label = stylePair.first.id
        ) {
            // Call your StyleBox composable for the left style
            StyleBox(selectedStyleId, stylePair.first, onSelected)
        }



        AnimatedVisibility(
            visible = showSecond,
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            label = stylePair.second.id
        ) {
            // Call your StyleBox composable for the right style
            StyleBox(selectedStyleId, stylePair.second, onSelected)
        }
    }

}


@Composable
fun StyleBox(selectedStyleId: String?, style: Style, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .selectable(
                selected = selectedStyleId == style.id,
                onClick = { onClick(style.id) }
            )
            .alpha(if (selectedStyleId == null || selectedStyleId == style.id) 1f else 0.5f)
            .border(
                width = if (selectedStyleId == null) 0.dp else 2.dp,
                color = when (selectedStyleId) {
                    null -> Color.Transparent
                    style.id -> Color.Green
                    else -> Color.Red
                },
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        AsyncImage(
            model = style.url,
            contentScale = ContentScale.Crop,
            contentDescription = style.id,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(9f / 16f)
        )
    }
}
