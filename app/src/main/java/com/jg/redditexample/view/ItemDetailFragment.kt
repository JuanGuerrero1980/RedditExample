package com.jg.redditexample.view

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.jg.redditexample.R
import com.jg.redditexample.model.Post
import com.jg.redditexample.utils.loadImage
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*

class ItemDetailFragment : Fragment() {


    private var item: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                item = it.getSerializable(ARG_ITEM_ID) as Post?
                activity?.toolbar_layout?.title = item?.author
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)


        item?.let {
            rootView.textViewContent.text = it.title
            rootView.textViewAuthor.text = it.author
            rootView.imageViewThumb.loadImage(it.thumbnail)
            rootView.imageViewThumb.setOnClickListener { saveImage(rootView.imageViewThumb!!, item!!.id) }
        }

        return rootView
    }

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    private fun saveImage(image:View, title:String){

        val drawable = (image as ImageView).drawable
        val bitmap = (drawable as BitmapDrawable).bitmap
        val savedImageURL = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            bitmap,
            title,
            "Image of $title"
        )
        Toast.makeText(activity, savedImageURL, Toast.LENGTH_SHORT).show()
    }
}
