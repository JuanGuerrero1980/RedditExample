package com.jg.redditexample.view

import android.content.Intent
import android.os.Bundle
import android.system.Os.remove
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.jg.redditexample.R
import com.jg.redditexample.data.Children
import com.jg.redditexample.utils.loadImage
import kotlinx.android.synthetic.main.item_list_content.view.*

class SimpleItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                    private var values: MutableList<Children>,
                                    private val twoPane: Boolean) :
    RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Children
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_ID, item.data.title)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, item.data.title)
                }
                v.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.authorView.text = item.data.author
        holder.contentView.text = item.data.title
        holder.commentsView.text = item.data.num_comments.toString()
        holder.createdView.text = item.data.created.toString()
        holder.imageView.loadImage(item.data.thumbnail)
        with(holder.contentLayoutView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    fun update(data:List<Children>){
        values.addAll(data)
        notifyDataSetChanged()
    }



    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.title
        val authorView: TextView = view.author
        val commentsView: TextView = view.comments
        val createdView: TextView = view.created
        val imageView: ImageView = view.imageViewThumb
        val dismissView: TextView = view.dismiss
        val contentLayoutView: LinearLayout = view.contentLayout

        init {
            dismissView.setOnClickListener(remove())
        }

        private fun remove(): (View) -> Unit = {
            layoutPosition.also { currentPosition ->
                values.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
            }
        }
    }
}