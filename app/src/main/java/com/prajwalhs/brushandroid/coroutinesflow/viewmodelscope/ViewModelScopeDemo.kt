package com.prajwalhs.brushandroid.coroutinesflow.viewmodelscope

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: viewModelScope
 * -----------------------
 * A CoroutineScope built into every ViewModel (androidx.lifecycle.viewModelScope).
 * Coroutines launched here are AUTOMATICALLY CANCELLED when the ViewModel is
 * cleared (e.g. the screen is removed from the back stack) — no manual
 * cleanup needed. This is the standard place to load data / auto-update UI state.
 *
 * Gradle deps needed:
 *   implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:<latest>")
 *   implementation("androidx.lifecycle:lifecycle-viewmodel-compose:<latest>")   // for viewModel()
 *   implementation("androidx.lifecycle:lifecycle-runtime-compose:<latest>")    // for collectAsStateWithLifecycle()
 */

class CounterViewModel : ViewModel() {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()

    fun startAutoIncrement() {
        viewModelScope.launch {
            repeat(5) {
                delay(1000.milliseconds)
                _count.update { it + 1 }
            }
            // If the screen is closed mid-way, this coroutine is cancelled
            // automatically — you never crash trying to update a dead UI.
        }
    }
}

@Composable
fun ViewModelScopeScreen(viewModel: CounterViewModel = viewModel()) {

    val count by viewModel.count.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("viewModelScope demo")
        Text("Count: $count")
        Button(onClick = { viewModel.startAutoIncrement() }) {
            Text("Start Auto Increment (+1 every second, 5 times)")
        }
    }
}