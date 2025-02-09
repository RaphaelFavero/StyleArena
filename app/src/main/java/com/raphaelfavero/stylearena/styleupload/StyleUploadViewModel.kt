package com.raphaelfavero.stylearena.styleupload

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StyleUploadViewModel @Inject constructor(private val repository: StyleUploadRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(
        StyleUploadState(
            styleUploaded = false,
            isLoading = false,
            error = false
        )
    )
    val state = _state.asStateFlow()

    fun uploadImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val sent = repository.uploadImage(bitmap)
            _state.update { it.copy(styleUploaded = sent, isLoading = false, error = !sent) }

        }
    }

    fun clearError() {
        _state.update { it.copy(error = false) }
    }
}

data class StyleUploadState(
    val styleUploaded: Boolean,
    val isLoading: Boolean,
    val error: Boolean
)