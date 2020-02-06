package com.jg.redditexample.data

import retrofit2.Call
import retrofit2.http.GET

interface ServicesApiInterface {

    @GET("/top/.json?limit=50")
    fun getTopPosts(): Call<PostsResponse>
}