package com.jg.redditexample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jg.redditexample.data.Data
import com.jg.redditexample.data.ResponseCallBack
import com.jg.redditexample.model.PostRepositoryRemote

class PostViewModel(private val repository: PostRepositoryRemote): ViewModel() {

    private val _posts = MutableLiveData<Data>().apply { value = null }
    val posts: LiveData<Data> = _posts

    private val _isViewLoading=MutableLiveData<Boolean>()
    val isViewLoading:LiveData<Boolean> = _isViewLoading

    private val _onMessageError=MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList= MutableLiveData<Boolean>()
    val isEmptyList:LiveData<Boolean> = _isEmptyList


    init {
        //loadTopPosts()
    }

    fun loadTopPosts(after: String, before: String){

        _isViewLoading.postValue(true)
        repository.retrieveTopPosts(after, before, object:ResponseCallBack{
            override fun onError(obj: Any?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue( obj)
            }

            override fun onSuccess(obj: Any?) {
                _isViewLoading.postValue(false)

                if(obj!=null && obj is Data){
                    if(obj.children.isEmpty()){
                        _isEmptyList.postValue(true)
                    }else{
                        _posts.value= obj as Data
                    }
                }
            }
        })
    }
}