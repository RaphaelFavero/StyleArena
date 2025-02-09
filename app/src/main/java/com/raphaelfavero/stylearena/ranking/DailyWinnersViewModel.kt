package com.raphaelfavero.stylearena.ranking

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
class DailyWinnersViewModel @Inject constructor(private val repository: DailyWinnersRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(
        DailyWinnersState(
            ranking = null,
            isLoading = false,
            error = false
        )
    )
    val state = _state.asStateFlow()

    fun getDailyWinners() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val ranking = repository.getDailyWinners()
            _state.update { it.copy(ranking = ranking, isLoading = false, error = ranking == null) }
        }
    }


}

data class DailyWinnersState(
    val ranking: Triple<Style, Style, Style>?,
    val isLoading: Boolean,
    val error: Boolean
)

