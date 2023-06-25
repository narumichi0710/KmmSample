package com.example.kmmsample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.kmmsample.android.GithubSearch.GithubSearchView
import com.example.kmmsample.android.GithubSearch.GithubSearchViewModel

class MainActivity : ComponentActivity() {

    private val searchViewModel: GithubSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GithubSearchView(searchViewModel)
                }
            }
        }
    }
}