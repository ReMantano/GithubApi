package com.example.kotlingithubapi.api

/**
 * Created by Vladislav on 06.06.2018.
 */
class SearchRepository(val apiService: GitHubImApiService) {

    fun searchFile(name: String, rep: String, path: String): io.reactivex.Observable<List<File>> {
        return apiService.searchFile( name,rep,path)
    }

    fun searchRepository(name: String): io.reactivex.Observable<List<Repository>> {
        return apiService.searchRepository(name)
    }

    fun searchUser(name : String ): io.reactivex.Observable<User> {
        return apiService.searchUser(name)
    }

    fun searchListUser(): io.reactivex.Observable<List<User>> {
        return apiService.searchListUser()
    }

    fun searchPeoples(name : String): io.reactivex.Observable<List<User>> {
        return apiService.searchPeoples(name)
    }

    fun searchReposytories(name : String): io.reactivex.Observable<List<Repository>> {
        return apiService.searchReposytories(name)
    }



}