package com.example.kotlingithubapi.api


import com.example.kotlingithubapi.adapter.ArrayAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



interface GitHubImApiService {

    @GET("/repos/{name}/{searchFile}/contents/{path}")
    fun searchFile(
            @Path("name") name: String,
            @Path("searchFile") repository: String,
            @Path("path") path:String): io.reactivex.Observable<List<File>>

    @GET("/users/{name}/repos")
    fun searchRepository(
            @Path("name") name: String): io.reactivex.Observable<List<Repository>>


    @GET("/users/{name}")
    fun searchUser(
            @Path("name") name: String  ): io.reactivex.Observable<User>

    @GET("/users")
    fun searchListUser(
    ): io.reactivex.Observable<List<User>>

    @GET("/search/users")
    fun searchPeoples(
            @Query("q") q:String ) : io.reactivex.Observable<List<User>>

    @GET("/search/repositories")
    fun searchReposytories(
            @Query("q") q:String) : io.reactivex.Observable<List<Repository>>

    companion object Factory {
        fun create(): GitHubImApiService {
            val gson: Gson = GsonBuilder().registerTypeAdapterFactory(ArrayAdapterFactory()).create()
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl("https://api.github.com")
                    .build();
            return retrofit.create(GitHubImApiService::class.java)

        }
    }

}