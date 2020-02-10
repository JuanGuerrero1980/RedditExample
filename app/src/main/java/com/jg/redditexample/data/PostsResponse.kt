package com.jg.redditexample.data

import com.jg.redditexample.model.Post


data class PostsResponse(val data:Data)

data class Data(val modhash:String, val dist:Int, var children:MutableList<Children>, var after:String, var before:String)

data class Children(val kind:String, val data:Post)
