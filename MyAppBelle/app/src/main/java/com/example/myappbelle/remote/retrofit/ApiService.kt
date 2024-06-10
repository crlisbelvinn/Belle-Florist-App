package com.example.myappbelle.remote.retrofit

import com.example.myappbelle.remote.response.DetailUserResponse
import com.example.myappbelle.remote.response.GithubResponse
import com.example.myappbelle.remote.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getAccount(@Query("q") username : String) : Call<GithubResponse>
    @GET("users/{username}/following")
    fun getAccountFollowing(@Path("username") username: String) : Call<List<ItemsItem>>
    @GET("users/{username}/followers")
    fun getAccountFollowers(@Path("username") username: String) : Call<List<ItemsItem>>
    @GET("users/{username}")
    fun getAccountDetail(@Path("username") username: String) : Call<DetailUserResponse>
}