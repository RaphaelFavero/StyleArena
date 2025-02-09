package com.raphaelfavero.stylearena.styleupload

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raphaelfavero.stylearena.R
import com.raphaelfavero.stylearena.core.ui.ErrorDialog
import com.raphaelfavero.stylearena.core.ui.OrbitLoadingAnimation

@Composable
fun StyleUploadScreen(onStyleUploaded: () -> Unit) {

    val viewModel: StyleUploadViewModel = hiltViewModel()
    val state: StyleUploadState by viewModel.state.collectAsStateWithLifecycle()

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        viewModel.uploadImage(bitmap!!)
    }


    when {
        state.styleUploaded -> {
            LaunchedEffect(Unit) {
                onStyleUploaded()
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

        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = R.string.style_picture_prompt),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
                Text(
                    text = stringResource(id = R.string.win_prizes_prompt),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
                Button(
                    onClick = { takePictureLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = stringResource(id = R.string.capture_style_button))
                }
            }

            if (state.error) {
                ErrorDialog(onDismiss = { viewModel.clearError() })
            }

        }
    }
}