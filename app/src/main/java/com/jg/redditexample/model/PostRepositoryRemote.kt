package com.jg.redditexample.model

import android.util.Log
import com.jg.redditexample.data.ApiClient
import com.jg.redditexample.data.PostsResponse
import com.jg.redditexample.data.ResponseCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepositoryRemote: PostRepository {

    private var call:Call<PostsResponse>?=null
    private val tag = PostRepositoryRemote::class.java.simpleName

    override fun retrieveTopPosts(callback: ResponseCallBack) {
        call= ApiClient.build()?.getTopPosts()
        call?.enqueue(object :Callback<PostsResponse>{
            override fun onFailure(call: Call<PostsResponse>, t: Throwable) {
                callback.onError(t.message)
            }

            override fun onResponse(call: Call<PostsResponse>, response: Response<PostsResponse>) {
                response?.body()?.let {
                    if(response.isSuccessful){
                        callback.onSuccess(it.data.children)
                    }else{
                        callback.onError("not found")
                    }
                }
            }
        })
    }

    override fun cancel() {
        call?.let { it.cancel() }
    }
}