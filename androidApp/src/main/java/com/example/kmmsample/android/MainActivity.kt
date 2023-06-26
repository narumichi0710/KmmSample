package com.example.kmmsample.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kmmsample.android.GithubSearch.GithubSearchView
import com.example.kmmsample.android.GithubSearch.GithubSearchViewModel
import com.example.kmmsample.android.Tutorial.GreetingView

class MainActivity : ComponentActivity() {

    private val searchViewModel: GithubSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun MainView() {
        MyApplicationTheme {
            val navController = rememberNavController()
            val entry by navController.currentBackStackEntryAsState()
            val currentRoute: String = entry?.destination?.route ?: ""

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(currentRoute) },
                        navigationIcon = {
                            if (currentRoute == Route.Home.localize)
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.Menu, contentDescription = "メニュー")
                                }
                            else
                                IconButton(onClick = { navController.popBackStack()  }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                                }
                        }
                    )
                }
            ) {
                NavHost(navController = navController, startDestination = Route.Home.localize) {
                    composable(Route.Home.localize) { Content(navController) }
                    composable(Route.Tutorial.localize) { GreetingView() }
                    composable(Route.Search.localize) { GithubSearchView(searchViewModel) }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun Content(navController: NavHostController) {
        LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
            items(Route.allPath) { item ->
                ListItem(
                    text = {
                        Text(
                            text = item.localize,
                        )
                    },
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable {
                            navController.navigate(item.localize)
                        }
                )
                Divider()
            }
        }
    }
    private enum class Route {
        Home,
        Tutorial,
        Search;
        val localize: String get() = when (this) {
            Home -> "KmmSample"
            Tutorial -> "チュートリアル"
            Search -> "検索"
        }
        companion object {
            var allPath = values().filterNot { it == Home }
        }
    }
}