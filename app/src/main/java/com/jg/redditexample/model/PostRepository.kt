package com.jg.redditexample.model

import com.jg.redditexample.data.ResponseCallBack

interface PostRepository {

    fun retrieveTopPosts(callback: ResponseCallBack)
    fun cancel()
}