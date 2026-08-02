package com.prajwalhs.brushandroid.coroutinesflow.runblockingdemo

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: runBlocking
 * -------------------
 * `runBlocking` starts a coroutine AND BLOCKS the current thread until it
 * finishes. It bridges regular blocking code with suspend functions.
 *
 * Do NOT use it inside Android app code that runs on the main thread — it
 * freezes the UI and can trigger an ANR (App Not Responding). Its real
 * homes are:
 *   1. `fun main()` in plain Kotlin scripts/apps
 *   2. Unit tests (bridging a blocking test method with suspend code)
 *   3. Deliberately blocking a background thread (rare, e.g. a CLI tool)
 *
 * The Composable below deliberately misuses runBlocking on the main thread
 * so you can literally FEEL the UI freeze — tap "Bad" and notice the status
 * text doesn't even update to "Blocking..." until the delay is fully over.
 * Compare it with "Good", which uses launch instead.
 */

// Safe usage: plain main(), run this function directly (not part of the app UI)
fun main() = runBlocking {
    println("Start")
    delay(1000.milliseconds)
    println("End - printed only after the full second, thread was blocked the whole time")
}

@Composable
fun RunBlockingScreen() {
    var status by remember { mutableStateOf("Idle") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("runBlocking demo")
        Text("Status: $status")

        Button(onClick = {
            status = "Blocking..."
            // ANTI-PATTERN: this BLOCKS the main/UI thread for 2s.
            // "Blocking..." above won't even render until this finishes,
            // because recomposition itself needs the main thread.
            runBlocking {
                delay(2000.milliseconds)
            }
            status = "Done (but UI was frozen for 2s)"
        }) {
            Text("Bad: runBlocking on main thread")
        }

        Button(onClick = {
            status = "Loading..."
            // CORRECT: launch suspends without blocking — UI stays responsive
            scope.launch {
                delay(2000)
                status = "Done (UI stayed responsive)"
            }
        }) {
            Text("Good: launch instead")
        }
    }
}