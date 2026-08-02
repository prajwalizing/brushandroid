package com.prajwalhs.brushandroid.coroutinesflow.exceptionhandling

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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * TOPIC: Exception handling in coroutines
 * ------------------------------------------
 * launch: an uncaught exception propagates IMMEDIATELY up the Job hierarchy.
 *         Install a CoroutineExceptionHandler on the scope/launch to catch it
 *         globally (e.g. to log a crash report or show a snackbar).
 *
 * async: an uncaught exception is STORED inside the Deferred and only
 *        re-thrown when you call .await() on it. If you never call await(),
 *        the failure can go unnoticed.
 */

private val handler = CoroutineExceptionHandler { _, exception ->
    println("Global handler caught: ${exception.message}")
}

@Composable
fun ExceptionHandlingScreen() {
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
        Text("Exception handling demo")

        Button(onClick = {
            log = emptyList()
            // launch + CoroutineExceptionHandler: catches it without crashing the app
            scope.launch(handler) {
                addLog("About to throw from launch")
                throw RuntimeException("Boom from launch")
            }
        }) { Text("Throw from launch (with handler)") }

        Button(onClick = {
            log = emptyList()
            scope.launch {
                val deferred = async {
                    addLog("About to throw from async")
                    throw RuntimeException("Boom from async")
                }
                try {
                    deferred.await() // exception is re-thrown HERE, not when async{} runs
                } catch (e: Exception) {
                    addLog("Caught at await(): ${e.message}")
                }
            }
        }) { Text("Throw from async (caught at await)") }

        log.forEach { Text(it) }
    }
}