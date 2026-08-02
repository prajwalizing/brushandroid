package com.prajwalhs.brushandroid.coroutinesflow.structuredconcurrency

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * TOPIC: Structured Concurrency
 * --------------------------------
 * The core idea behind Kotlin coroutines: every coroutine has a PARENT, and
 * a parent doesn't complete until ALL of its children complete. This gives
 * you two guarantees for free:
 *   1. No orphaned coroutines - the parent-child Job hierarchy tracks them
 *      even if you lose a direct reference.
 *   2. Cancel the parent -> every descendant is cancelled automatically.
 */

// Guarantee #1: coroutineScope suspends until BOTH children finish
suspend fun structuredExample(log: (String) -> Unit) {
    log("Before scope")
    coroutineScope {
        launch { delay(500); log("Child 1 done") }
        launch { delay(1000); log("Child 2 done") }
    }
    // This line is only reached after BOTH children above have completed
    log("After scope - both children finished")
}

// Guarantee #2: cancelling the parent Job cancels every descendant, recursively
fun startCancellableWork(scope: CoroutineScope, log: (String) -> Unit): Job {
    return scope.launch {
        launch {
            try {
                delay(5000)
                log("Grandchild finished normally") // only if never cancelled
            } finally {
                log("Grandchild cleanup ran - it WAS cancelled")
            }
        }
    }
}

@Composable
fun StructuredConcurrencyScreen() {
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
        Text("Structured concurrency demo")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    log = emptyList();
                    scope.launch {
                    structuredExample(::addLog)
                }
                })
            {
                Text("Run children")
            }
            Button(onClick = { log = emptyList(); job = startCancellableWork(scope, ::addLog) }) {
                Text("Start 5s work")
            }
            Button(onClick = { job?.cancel(); addLog("Parent cancelled") }) {
                Text("Cancel parent")
            }
        }
        log.forEach { Text(it) }
    }
}