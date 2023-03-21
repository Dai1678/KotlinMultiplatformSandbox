package dev.dai.kotlinmultiplatformsandbox.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.dai.kotlinmultiplatformsandbox.entity.RocketLaunch
import dev.dai.kotlinmultiplatformsandbox.shared.SpaceXSDK
import dev.dai.kotlinmultiplatformsandbox.shared.cache.DatabaseDriverFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val sdk = SpaceXSDK(DatabaseDriverFactory(application))

    init {
        displayLaunches(false)
    }

    fun displayLaunches(needReload: Boolean) {
        val currentState = uiState.value
        if (currentState.isRefreshing) return
        _uiState.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            kotlin.runCatching {
                sdk.getLaunches(needReload)
            }.onSuccess { launches ->
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        launches = launches
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        event = MainEvent.Error(exception)
                    )
                }
            }
        }
    }

    fun consumeEvent() {
        _uiState.update { it.copy(event = null) }
    }
}

data class MainUiState(
    val isRefreshing: Boolean = false,
    val launches: List<RocketLaunch> = emptyList(),
    val event: MainEvent? = null
)

sealed interface MainEvent {
    data class Error(val throwable: Throwable) : MainEvent
}
