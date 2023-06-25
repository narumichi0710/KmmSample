package com.example.kmmsample.android.Tutorial

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.kmmsample.Tutorial.Greeting


@Composable
fun GreetingView() {
    var text by remember { mutableStateOf("Loading") }
    LaunchedEffect(true) {
        text = try {
            Greeting().greet()
        } catch (e: Exception) {
            e.localizedMessage ?: "error"
        }
    }

    Text(text = text)
}