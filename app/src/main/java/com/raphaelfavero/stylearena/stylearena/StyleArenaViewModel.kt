package com.raphaelfavero.stylearena.stylearena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raphaelfavero.stylearena.core.network.Style
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StyleArenaViewModel @Inject constructor(private val repository: StyleArenaRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(
        StyleArenaState(
            stylePair = null,
            hasVoted = false,
            isLoading = false,
            error = false
        )
    )
    val state = _state.asStateFlow()

    fun getStylePair() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val pair = repository.getStylePair()
            _state.update { it.copy(stylePair = pair, isLoading = false, error = pair == null) }
        }
    }

    fun vote(styleId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val hasVoted = repository.vote(styleId)
            _state.update { it.copy(hasVoted = hasVoted, isLoading = false, error = !hasVoted) }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = false) }
    }

}

data class StyleArenaState(
    val stylePair: Pair<Style, Style>?,
    val hasVoted: Boolean,
    val isLoading: Boolean,
    val error: Boolean
)

