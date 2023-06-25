package com.example.kmmsample.android.GithubSearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.example.kmmsample.GithubSearch.GithubSearch
import com.example.kmmsample.GithubSearch.SearchResult
import com.example.kmmsample.GithubSearch.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GithubSearchViewModel: ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _result = MutableStateFlow<SearchResult?>(null)
    val result: StateFlow<SearchResult?> = _result

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
    }

    fun fetch() {
        viewModelScope.launch {
            val result = GithubSearch().request(_searchText.value)
            _result.value = result
        }
    }
}

@Composable
fun GithubSearchView(viewModel: GithubSearchViewModel = GithubSearchViewModel()) {
    val searchText by viewModel.searchText.collectAsState()
    val result by viewModel.result.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Search text field
        OutlinedTextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
            label = { Text("user name") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Ascii),
            modifier = Modifier
                .padding()
                .fillMaxWidth()
        )
        Button(onClick = { viewModel.fetch() }) {
            Text("Search")
        }
        Spacer(modifier = Modifier.height(16.dp))

        result?.let {
            LazyColumn {
                items(it.users) {
                    UserCell(it)
                    Divider()
                }
            }
        } ?: Text("検索ワードに一致するユーザーが存在しません。")
    }
}

@Composable
fun UserCell(user: User) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatarUrl),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(user.login)
    }
}