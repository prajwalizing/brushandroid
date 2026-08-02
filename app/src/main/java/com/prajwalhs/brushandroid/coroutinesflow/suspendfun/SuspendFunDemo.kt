package com.prajwalhs.brushandroid.coroutinesflow.suspendfun

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
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: suspend fun
 * -------------------
 * A `suspend` function can PAUSE and RESUME without blocking the thread it's
 * running on. It can only be called from another suspend function or from a
 * coroutine (launch/async/runBlocking).
 *
 * Here `fetchUserName()` simulates a network call using delay() — a suspend
 * function itself. While it's "waiting", the calling thread (e.g. Main) is
 * free to do other work (like keep the UI responsive/scrolling).
 */


@Composable
fun SuspendFunScreen() {
    var result by remember { mutableStateOf("Press the button to fetch") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("suspend fun demo")
        Text(result)
        Button(
            onClick = {
                scope.launch {                  // coroutine builder — required to CALL a suspend fun
                    loading = true
                    result = "Loading..."
                    val name = fetchUserName()  // suspension point — UI stays responsive here
                    result = "Fetched: $name"
                    loading = false
                }
            }, enabled = !loading
        ) {
            Text(if (loading) "Fetching..." else "Fetch User Name")
        }
    }
}

// A plain suspend function — no coroutine builder needed to DEFINE it,
// only to CALL it.
suspend fun fetchUserName(): String {
    delay(1500.milliseconds) // simulates network/DB latency, does NOT block the thread
    return "Sandeep"
}