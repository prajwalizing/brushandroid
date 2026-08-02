package com.prajwalhs.brushandroid.coroutinesflow.withcontextdemo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * TOPIC: withContext
 * --------------------
 * `withContext` SWITCHES the coroutine's dispatcher (thread pool) for a block
 * of code and SUSPENDS until that block finishes, returning its result —
 * without launching a brand-new coroutine. Use it inside an existing suspend
 * function to move work on/off the main thread as needed.
 *
 *   Dispatchers.Main    -> UI updates
 *   Dispatchers.IO      -> network/disk (many threads, blocking-friendly)
 *   Dispatchers.Default -> CPU-heavy work (sized to CPU core count)
 */

// IO-bound work: simulate reading a large file from disk
suspend fun readFromDiskSimulated(): String = withContext(Dispatchers.IO) {
    delay(1000)
    "Disk read done on thread: ${Thread.currentThread().name}"
}

// CPU-bound work: simulate a heavy calculation
suspend fun heavyComputation(): String = withContext(Dispatchers.Default) {
    var sum = 0L
    for (i in 1..50_000_000) sum += i
    "Sum=$sum computed on thread: ${Thread.currentThread().name}"
}

@Composable
fun WithContextScreen() {
    var result by remember { mutableStateOf("Press a button") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("withContext demo")
        Text("Caller thread: ${Thread.currentThread().name}")
        Text(result)

        Button(onClick = {
            scope.launch {
                result = "Reading..."
                result = readFromDiskSimulated() // hops to IO, then returns to Main automatically
            }
        }) { Text("Simulate Disk Read (Dispatchers.IO)") }

        Button(onClick = {
            scope.launch {
                result = "Computing..."
                result = heavyComputation() // hops to Default, then returns to Main automatically
            }
        }) { Text("Simulate Heavy Compute (Dispatchers.Default)") }
    }
}