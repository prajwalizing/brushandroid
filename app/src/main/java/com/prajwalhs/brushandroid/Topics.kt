package com.prajwalhs.brushandroid

import androidx.compose.runtime.Composable
import com.prajwalhs.brushandroid.coroutinesflow.launchbuilder.LaunchDemoScreen
import com.prajwalhs.brushandroid.coroutinesflow.suspendfun.SuspendFunScreen

class Topics{
    val topics: List<Pair<String, @Composable () -> Unit>> = listOf(
        "1. suspend fun" to { SuspendFunScreen() },
        "2. launch {}" to { LaunchDemoScreen() },
//        "3. async / await" to { AsyncAwaitScreen() },
//        "4. runBlocking" to { RunBlockingScreen() },
//        "5. withContext" to { WithContextScreen() },
//        "6. viewModelScope" to { ViewModelScopeScreen() },
//        "7. CoroutineScope variants" to { CoroutineScopeVariantsScreen() },
//        "8. SupervisorJob" to { SupervisorJobScreen() },
//        "9. Structured concurrency" to { StructuredConcurrencyScreen() },
//        "10. Exception handling" to { ExceptionHandlingScreen() },
//        "11. Parallel API calls" to { ParallelApiCallsScreen() },
//        "12. return@launch / return@collect" to { ReturnLabelsScreen() },
//        "13. flowOn" to { FlowOnScreen() },
    )

}