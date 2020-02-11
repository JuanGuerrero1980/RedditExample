package com.jg.redditexample.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jg.redditexample.R


fun ImageView.loadImage(uri: String?) {
    val options = RequestOptions()
        .placeholder(R.drawable.ic_launcher_background)
        .error(R.mipmap.ic_launcher_round)
    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}
