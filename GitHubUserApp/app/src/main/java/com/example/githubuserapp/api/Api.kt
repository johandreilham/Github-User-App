package com.example.githubuserapp.api

import com.example.githubuserapp.data.DetailUserResponse
import com.example.githubuserapp.data.GitHub
import com.example.githubuserapp.data.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @GET("search/users")
    //@Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ) : Call<UserResponse>

    @GET("users/{username}")
   // @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ) : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    //@Headers("Authorization: token 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")
    fun getFollowers(
        @Path("username") username: String
    ) : Call<ArrayList<GitHub>>

    @GET("users/{username}/following")
    //@Headers("Authorization: token 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")
    fun getFollowing(
        @Path("username") username: String
    ) : Call<ArrayList<GitHub>>
}