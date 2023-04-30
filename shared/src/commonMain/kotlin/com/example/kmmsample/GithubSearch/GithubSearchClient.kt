package com.example.kmmsample.GithubSearch

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class SearchUser(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("incomplete_results")
    val incompleteResults: String,
    val items: List<Item>
)

@Serializable
data class Item(
    val login: String,
    @SerialName("avatar_url")
    val avatarUrl: String,
    @SerialName("repos_url")
    val reposUrl: String
)

class GithubSearch {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    private val baseUrl = "https://api.github.com/"
    private val path = "search/repositories/"
    private fun createBuilder(query: Pair<String, Any>): URLBuilder {
        val builder = URLBuilder(baseUrl)
        builder.path(path)
        builder.parameters.append(query.first, "${query.second}")
        return builder
    }

    @Throws(Exception::class)
    suspend fun request(text: String): SearchUser {
        val query = Pair("q", "text")
        val builder = createBuilder(query)
        val response = client.get(builder.build())
        return response.body()
    }
}