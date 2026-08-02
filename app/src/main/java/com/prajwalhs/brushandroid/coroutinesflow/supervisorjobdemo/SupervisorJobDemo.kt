package com.prajwalhs.brushandroid.coroutinesflow.supervisorjobdemo

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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: SupervisorJob
 * -----------------------
 * With a REGULAR Job (what coroutineScope/launch use by default), if ONE
 * child fails with an uncaught exception, the whole scope is cancelled —
 * including all sibling children.
 *
 * With a SupervisorJob (or supervisorScope), a child's failure does NOT
 * cancel its siblings. Each child is independent — but you must still
 * handle each child's own exceptions (try/catch inside it, or a
 * CoroutineExceptionHandler), otherwise it just fails silently/crashes.
 *
 * Typical use: a screen with several independent widgets/sections, where one
 * failing to load shouldn't take the other sections down with it.
 */

suspend fun demoRegularJob(log: (String) -> Unit) {
    try {
        coroutineScope {
            launch {
                delay(200)
                throw RuntimeException("Child A failed")
            }
            launch {
                delay(500)
                log("Child B completed") // never printed - sibling failure cancels this too
            }
        }
    } catch (e: Exception) {
        log("Regular Job -> whole scope cancelled: ${e.message}")
    }
}

suspend fun demoSupervisorJob(log: (String) -> Unit) {
    supervisorScope {
        launch {
            delay(200.milliseconds)
            try {
                throw RuntimeException("Child A failed")
            } catch (e: Exception) {
                log("SupervisorJob -> Child A caught its own error: ${e.message}")
            }
        }
        launch {
            delay(500.milliseconds)
            log("SupervisorJob -> Child B completed normally") // DOES print
        }
    }
}

@Composable
fun SupervisorJobScreen() {
    var log by remember { mutableStateOf(listOf<String>()) }
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
        Text("SupervisorJob demo")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { log = emptyList(); scope.launch { demoRegularJob(::addLog) } }) {
                Text("Regular Job")
            }
            Button(onClick = { log = emptyList(); scope.launch { demoSupervisorJob(::addLog) } }) {
                Text("SupervisorJob")
            }
        }
        log.forEach { Text(it) }
    }
}