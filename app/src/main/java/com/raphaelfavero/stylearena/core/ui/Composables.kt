package com.raphaelfavero.stylearena.core.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.raphaelfavero.stylearena.R


@Composable
fun OrbitLoadingAnimation(modifier: Modifier = Modifier) {
    val outerCirclePaddingPercentage = 0.15f
    val innerCirclePaddingPercentage = 0.3f
    val outerCircleOffsetStart = 90f
    val innerCircleOffsetStart = 135f
    val strokeWidth = 1.5.dp

    val infiniteTransition = rememberInfiniteTransition(label = "orbit-transition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 1000)),
        label = "orbit-rotation"
    )

    var width by remember {
        mutableIntStateOf(0)
    }
    Box(
        modifier = modifier
            .size(40.dp)
            .onSizeChanged {
                width = it.width
            },
        contentAlignment = Alignment.Center,

        ) {

        CircularProgressIndicator(
            strokeWidth = strokeWidth,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    with(LocalDensity.current) {
                        (width * innerCirclePaddingPercentage).toDp()
                    }
                )
                .graphicsLayer {
                    // using rotate() would cause recomposition but not graphicsLayer
                    rotationZ = rotation + innerCircleOffsetStart
                }
        )

        CircularProgressIndicator(
            strokeWidth = strokeWidth,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    with(LocalDensity.current) {
                        (width * outerCirclePaddingPercentage).toDp()
                    }
                )
                .graphicsLayer {
                    // using rotate() would cause recomposition but not graphicsLayer
                    rotationZ = rotation + outerCircleOffsetStart
                }
        )

        CircularProgressIndicator(
            strokeWidth = strokeWidth,
            modifier = Modifier
                .fillMaxSize()

                .graphicsLayer {
                    // using rotate() would cause recomposition but not graphicsLayer
                    rotationZ = rotation
                }
        )
    }
}

@Composable
fun ErrorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        dismissButton = {},
        text = {
            Text(text = stringResource(id = R.string.request_error))
        },
        properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
    )
}