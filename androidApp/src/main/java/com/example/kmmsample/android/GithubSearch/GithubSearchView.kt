package com.example.kmmsample.android.GithubSearch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _result = MutableStateFlow<SearchResult?>(null)
    val result: StateFlow<SearchResult?> = _result

    fun onSearchTextChanged(newText: String) {
        _searchText.value = newText
    }
    private fun toggle() {
        _isLoading.value = !_isLoading.value
    }

    fun fetch() {
        viewModelScope.launch {
            toggle()
            val result = GithubSearch().request(_searchText.value)
            _result.value = result
            toggle()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GithubSearchView(viewModel: GithubSearchViewModel = GithubSearchViewModel()) {
    val searchText by viewModel.searchText.collectAsState()
    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp).height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = viewModel::onSearchTextChanged,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        apply(
                            viewModel,
                            keyboardController,
                            focusManager
                        )
                    })
                )

                Button(
                    modifier = Modifier.fillMaxSize().padding(start = 8.dp),
                    onClick = {
                    apply(
                        viewModel,
                        keyboardController,
                        focusManager
                    )
                }) {
                    Text("Search")
                }
            }

            result?.let {
                if (it.users.isEmpty()) {
                    Text(
                        text = "検索ワードに一致するユーザーが存在しません。",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                } else {
                    LazyColumn {
                        items(it.users) {
                            UserCell(it)
                            Divider()
                        }
                    }
                }
            }
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
private fun apply(
    viewModel: GithubSearchViewModel,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    keyboardController?.hide()
    focusManager.clearFocus()
    viewModel.fetch()
}

@Composable
private fun UserCell(user: User) {
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