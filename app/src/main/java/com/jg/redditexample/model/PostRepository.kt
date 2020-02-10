package com.jg.redditexample.model

import com.jg.redditexample.data.ResponseCallBack

interface PostRepository {

    fun retrieveTopPosts(after: String, before: String, callback: ResponseCallBack)
    fun cancel()
}