package com.jg.redditexample.model

import java.io.Serializable

data class Post(val id:String, val title:String, val author:String, val thumbnail:String, val num_comments:Int, val created:Long, var unreadStatus:Boolean) : Serializable