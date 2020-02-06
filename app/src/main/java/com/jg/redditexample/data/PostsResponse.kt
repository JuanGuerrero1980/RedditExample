package com.jg.redditexample.data

import com.jg.redditexample.model.Post


data class PostsResponse(val data:Data)

data class Data(val modhash:String, val dist:Int, val children:List<Children>)

data class Children(val kind:String, val data:Post)
