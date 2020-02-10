package com.jg.redditexample.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jg.redditexample.R
import com.jg.redditexample.data.Children
import com.jg.redditexample.data.Data

import com.jg.redditexample.model.PostRepositoryRemote
import com.jg.redditexample.viewmodel.PostViewModel
import com.jg.redditexample.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.item_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var viewModel: PostViewModel
    private lateinit var adapter: SimpleItemRecyclerViewAdapter
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var page: String? = ""

    companion object {
        const val TAG= "View"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            twoPane = true
        }

        setupViewModel()
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        adapter = SimpleItemRecyclerViewAdapter(this, viewModel.posts.value?.children ?: mutableListOf(), twoPane)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                if(!page.isNullOrEmpty()) {
                    isLoading = true
                    viewModel.loadTopPosts(after = "", before = page!!)
                }
            }
        })
    }


    private fun setupViewModel(){
        viewModel = ViewModelProviders.of(this, ViewModelFactory(repository = PostRepositoryRemote())).get(PostViewModel::class.java)
        viewModel.posts.observe(this,renderPosts)

        //viewModel.isViewLoading.observe(this,isViewLoadingObserver)
        //viewModel.onMessageError.observe(this,onMessageErrorObserver)
        //viewModel.isEmptyList.observe(this,emptyListObserver)
    }

    private val renderPosts = Observer<Data> {
        Log.v(TAG, "data updated $it")
        if(it == null) return@Observer
        //layoutError.visibility=View.GONE
        //layoutEmpty.visibility=View.GONE
        page = it.after
        adapter.update(it.children)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTopPosts("", "")
    }

}
