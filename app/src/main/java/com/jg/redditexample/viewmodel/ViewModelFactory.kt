package com.jg.redditexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jg.redditexample.model.PostRepositoryRemote

class ViewModelFactory(private val repository:PostRepositoryRemote): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostViewModel(repository) as T
    }
}