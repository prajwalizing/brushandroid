package com.prajwalhs.brushandroid.coroutinesflow.launchbuilder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: launch {}
 * -----------------
 * `launch` starts a new coroutine WITHOUT blocking the caller and returns a
 * `Job` you can use to cancel it or check its state. Use `launch` when you
 * don't need a RESULT back (fire-and-forget) — e.g. logging analytics,
 * starting a background sync, updating UI state as a side effect.
 */

@Composable
fun LaunchDemoScreen() {
    var log by remember { mutableStateOf(listOf<String>()) }
    var job: Job? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    fun addLog(msg: String) {
        log = log + msg
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("launch {} demo")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                job = scope.launch {
                    addLog("Started long task")
                    delay(3000.milliseconds)
                    addLog("Finished long task") // won't print if cancelled first
                }
            }) { Text("Start") }

            Button(onClick = {
                job?.cancel()               // cooperative cancellation
                addLog("Cancelled: isCancelled=${job?.isCancelled}")
            }) { Text("Cancel") }
        }
        log.forEach { Text(it) }
    }
}