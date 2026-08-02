package com.prajwalhs.brushandroid.coroutinesflow.returnlabels

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

/**
 * TOPIC: return@launch / return@collect
 * -----------------------------------------
 * These are LABELED returns. A lambda passed to launch{} or collect{} is
 * still just a lambda - a plain `return` inside it would try to return from
 * the nearest enclosing FUNCTION, which the compiler won't allow here.
 * `return@launch` / `return@collect` exits only THAT lambda:
 *   - inside launch{}: acts like an early "exit" from that coroutine's body
 *   - inside collect{}: acts like "continue" - skips to the next emitted value
 */

suspend fun demoReturnAtCollect(log: (String) -> Unit) {
    val numbers: Flow<Int> = flowOf(1, 2, 3, 4, 5)
    numbers.collect { value ->
        if (value == 3) {
            log("Skipping 3")
            return@collect // skip just this emission, keep collecting the rest
        }
        log("Collected: $value")
    }
}

@Composable
fun ReturnLabelsScreen() {
    var log by remember { mutableStateOf(listOf<String>()) }
    val scope = rememberCoroutineScope()

    fun addLog (msg: String) {
        log = log + msg
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("return@launch / return@collect demo")

        Button(onClick = {
            log = emptyList()
            scope.launch {
                addLog("Start")
                val skipRest = true
                if (skipRest) {
                    addLog("Condition met - exiting this launch block early")
                    return@launch // exits only this lambda, not ReturnLabelsScreen()
                }
                addLog("This line never runs")
            }
        }) { Text("Demo return@launch") }

        Button(onClick = {
            log = emptyList()
            scope.launch { demoReturnAtCollect(::addLog) }
        }) { Text("Demo return@collect") }

        log.forEach {
            Text(it)
        }
    }
}