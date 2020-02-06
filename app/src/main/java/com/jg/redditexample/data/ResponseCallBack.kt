package com.jg.redditexample.data

interface ResponseCallBack {
    fun onSuccess(obj:Any?)
    fun onError(obj:Any?)
}