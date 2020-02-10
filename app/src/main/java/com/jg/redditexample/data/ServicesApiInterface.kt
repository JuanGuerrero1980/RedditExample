package com.jg.redditexample.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServicesApiInterface {

    @GET("/top/.json")
    fun getTopPosts(@Query("after") after: String? = null,
                    @Query("before") before: String? = null,
                    @Query("limit") limit: Int = 10): Call<PostsResponse>
}