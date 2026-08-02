package com.prajwalhs.brushandroid.coroutinesflow.asyncawait

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: async {} / await()
 * ---------------------------
 * `async` starts a coroutine that returns a `Deferred<T>` — a "promise" of a
 * future result. Call `.await()` to suspend until the result is ready.
 * Use `async` when you DO need a return value, especially when running
 * multiple independent suspend calls CONCURRENTLY.
 */

suspend fun fetchTemperature(): Int {
    delay(1000.milliseconds) // simulate 1s network call
    return 28
}

suspend fun fetchHumidity(): Int {
    delay(1000.milliseconds) // simulate 1s network call
    return 65
}

@Composable
fun AsyncAwaitScreen() {
    var result by remember { mutableStateOf("Press the button to fetch weather") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("async {} / await() demo")

        Text(result)

        Button(
            onClick = {
                scope.launch {
                    val start = System.currentTimeMillis()
                    result = "Loading concurrently..."

                    // Both start immediately — they run CONCURRENTLY, not one after another
                    val temperature = async { fetchTemperature() }
                    val humidity = async { fetchHumidity() }

                    // await() suspends until each Deferred completes
                    val temp = temperature.await()
                    val hum = humidity.await()

                    val elapsed = System.currentTimeMillis() - start
                    result = "Temp: $temp°C, Humidity: $hum% (took ~${elapsed}ms, not ~2000ms)"
                }
            }
        ) {
            Text("Fetch Weather (concurrent)")
        }
    }
}