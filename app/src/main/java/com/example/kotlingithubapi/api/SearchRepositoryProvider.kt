package com.example.kotlingithubapi.api

/**
 * Created by Vladislav on 06.06.2018.
 */
object SearchRepositoryProvider {

    fun provideSearchRepository(): SearchRepository {
        return SearchRepository(GitHubImApiService.create())
    }
}