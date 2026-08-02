package com.prajwalhs.brushandroid.coroutinesflow.flowon

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * TOPIC: .flowOn()
 * -------------------
 * `flowOn` changes the dispatcher used for everything ABOVE it in the chain
 * (the flow builder + upstream operators) - it does NOT affect the
 * `collect {}` block, which always runs on whatever dispatcher the COLLECTOR
 * is using (e.g. Main, since we call collect from a Composable's coroutine
 * scope here).
 */

fun numberFlow(log: (String) -> Unit): Flow<Int> = flow {
    for (i in 1..3) {
        log("Emitting $i on ${Thread.currentThread().name}") // upstream - affected by flowOn below
        delay(300)
        emit(i)
    }
}.flowOn(Dispatchers.IO) // only affects the flow{} builder above, not the collector below

@Composable
fun FlowOnScreen() {
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
        Text(".flowOn() demo")
        Button(onClick = {
            log = emptyList()
            scope.launch {
                numberFlow(::addLog).collect { value ->
                    // downstream - stays on the collector's dispatcher (Main here)
                    addLog("Collected $value on ${Thread.currentThread().name}")
                }
            }
        }) { Text("Run Flow") }

        log.forEach { Text(it) }
    }
}