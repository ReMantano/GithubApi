package com.example.kotlingithubapi.api

import com.google.gson.annotations.SerializedName
import java.util.*




data class File(
        @SerializedName("path") val path: String,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String

)



data class Repository(

        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("watchers") val watchers: String,
        @SerializedName("language") val language: String,
        @SerializedName("forks") val forks: String,
        @SerializedName("items") val items: List<Repository>,
        @SerializedName("created_at") val created_at: Date

)



data class User(

        @SerializedName("login") val login: String,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String,
        @SerializedName("avatar_url") val avatar_url: String,
        @SerializedName("followers") val followers: String,
        @SerializedName("public_repos") val public_repos: String,
        @SerializedName("items") val items: List<User>,
        @SerializedName("location") val location: String

)