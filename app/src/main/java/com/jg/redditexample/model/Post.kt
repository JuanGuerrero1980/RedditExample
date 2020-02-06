package com.jg.redditexample.model

import java.io.Serializable

data class Post(val title:String, val author:String, val thumbnail:String, val num_comments:Int, val created:Long, val unreadStatus:Boolean) : Serializable