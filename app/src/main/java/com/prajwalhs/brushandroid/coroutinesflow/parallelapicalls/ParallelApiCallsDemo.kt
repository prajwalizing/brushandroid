package com.prajwalhs.brushandroid.coroutinesflow.parallelapicalls

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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

/**
 * TOPIC: Parallel API calls
 * -----------------------------
 * A very common interview scenario: a screen needs data from TWO independent
 * endpoints (e.g. user profile + user posts). Calling them one after another
 * wastes time; running them concurrently with async/await cuts total latency
 * down to roughly the SLOWER of the two calls, not the SUM of both.
 */

data class UserProfile(val name: String)
data class UserPosts(val count: Int)

suspend fun fetchProfile(): UserProfile {
    delay(1000.milliseconds)
    return UserProfile("Sandeep")
}

suspend fun fetchPosts(): UserPosts {
    delay(1200.milliseconds)
    return UserPosts(count = 42)
}

// ~2200ms total: one call waits for the other to fully finish first
suspend fun loadSequential(): Pair<UserProfile, UserPosts> {
    val profile = fetchProfile()
    val posts = fetchPosts()
    return profile to posts
}

// ~1200ms total: both requests are in flight at the same time
suspend fun loadParallel(): Pair<UserProfile, UserPosts> = coroutineScope {
    val profileDeferred = async { fetchProfile() }
    val postsDeferred = async { fetchPosts() }
    profileDeferred.await() to postsDeferred.await()
}

@Composable
fun ParallelApiCallsScreen() {
    var result by remember { mutableStateOf("Press a button") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Parallel API calls demo")
        Text(result)

        Button(onClick = {
            result = "Fetching Sequentially..."
            scope.launch {
                val start = System.currentTimeMillis()
                val (profile, posts) = loadSequential()
                val elapsed = System.currentTimeMillis() - start
                result = "Sequential: ${profile.name}, ${posts.count} posts - took ~${elapsed}ms"
            }
        }) { Text("Load Sequentially") }

        Button(onClick = {
            result = "Fetching Parallelly..."
            scope.launch {
                val start = System.currentTimeMillis()
                val (profile, posts) = loadParallel()
                val elapsed = System.currentTimeMillis() - start
                result = "Parallel: ${profile.name}, ${posts.count} posts - took ~${elapsed}ms"
            }
        }) { Text("Load in Parallel") }
    }
}