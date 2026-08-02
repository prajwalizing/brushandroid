package com.prajwalhs.brushandroid.coroutinesflow.scopevariants


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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: CoroutineScope variants
 * --------------------------------
 * A CoroutineScope defines a coroutine's LIFETIME. Which one you use depends
 * on what should own that lifetime:
 *
 *  - GlobalScope              -> lives as long as the app process. AVOID in
 *                                real code: nothing cancels it automatically,
 *                                easy to leak.
 *  - viewModelScope           -> tied to a ViewModel (see topic #6)
 *  - lifecycleScope           -> tied to an Activity/Fragment's lifecycle
 *  - rememberCoroutineScope()  -> tied to where a Composable is in the tree;
 *                                cancelled when that Composable leaves composition
 *  - Custom CoroutineScope    -> for classes like Repositories that aren't
 *                                tied to any Android lifecycle object; YOU decide
 *                                when to cancel it (see MyRepository below).
 */

// A non-Android class (e.g. a Repository) managing its own scope:
class MyRepository {
    // SupervisorJob so one failed task doesn't cancel the others (see topic #8)
    // Dispatchers.IO since this class does network/disk work by default
    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun startBackgroundSync(onDone: (String) -> Unit) {
        repoScope.launch {
            delay(800.milliseconds)
            onDone("Sync finished on ${Thread.currentThread().name}")
        }
    }

    // Call this yourself when the repository is no longer needed
    // (e.g. from ViewModel.onCleared(), or app shutdown)
    fun clear() {
        repoScope.cancel()
    }
}

@Composable
fun CoroutineScopeVariantsScreen() {
    var log by remember { mutableStateOf("Nothing yet") }
    // rememberCoroutineScope(): lives exactly as long as this Composable does.
    // If the user navigates away mid-task, this scope (and its coroutines) is cancelled.
    val composableScope = rememberCoroutineScope()
    val repository = remember { MyRepository() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("CoroutineScope variants demo")
        Text(log)

        Button(
            onClick = {
                composableScope.launch {
                    log = "Running in rememberCoroutineScope..."
                    delay(500)
                    log = "Done via rememberCoroutineScope"
                }
            }) { Text("Use rememberCoroutineScope()") }

        Button(onClick = {
            repository.startBackgroundSync { message -> log = message }
        }) { Text("Use custom Repository scope") }
    }
}