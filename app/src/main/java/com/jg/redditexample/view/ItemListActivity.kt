package com.jg.redditexample.view

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jg.redditexample.R
import com.jg.redditexample.data.Data
import com.jg.redditexample.model.PostRepositoryRemote
import com.jg.redditexample.viewmodel.PostViewModel
import com.jg.redditexample.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlin.time.ExperimentalTime

@ExperimentalTime
class ItemListActivity : AppCompatActivity() {

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

        if (item_detail_container != null) {
            twoPane = true
        }

        setupViewModel()
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        adapter = SimpleItemRecyclerViewAdapter(this, viewModel.posts.value?.children ?: mutableListOf(), twoPane)
        recyclerView.adapter = adapter
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    this@ItemListActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
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
        if(it == null) {
            page = ""
            adapter.clearAll()
            return@Observer
        }
        //layoutError.visibility=View.GONE
        //layoutEmpty.visibility=View.GONE
        page = it.after
        adapter.update(it.children)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_all -> {
                viewModel.clearPosts()
                Toast.makeText(applicationContext, "All items cleared!", Toast.LENGTH_LONG).show()
                true
            }else -> super.onOptionsItemSelected(item)
        }
    }
}
