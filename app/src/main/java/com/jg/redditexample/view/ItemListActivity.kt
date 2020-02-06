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
import com.jg.redditexample.R
import com.jg.redditexample.data.Children
import com.jg.redditexample.data.ResponseCallBack
import com.jg.redditexample.model.Post

import com.jg.redditexample.view.dummy.DummyContent
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
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupViewModel()
        setupRecyclerView(item_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        adapter = SimpleItemRecyclerViewAdapter(this, viewModel.posts.value?: emptyList(), twoPane)
        recyclerView.adapter = adapter
    }


    private fun setupViewModel(){
        viewModel = ViewModelProviders.of(this, ViewModelFactory(repository = PostRepositoryRemote())).get(PostViewModel::class.java)
        viewModel.posts.observe(this,renderPosts)

        //viewModel.isViewLoading.observe(this,isViewLoadingObserver)
        //viewModel.onMessageError.observe(this,onMessageErrorObserver)
        //viewModel.isEmptyList.observe(this,emptyListObserver)
    }

    private val renderPosts = Observer<List<Children>> {
        Log.v(TAG, "data updated $it")
        //layoutError.visibility=View.GONE
        //layoutEmpty.visibility=View.GONE
        adapter.update(it)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTopPosts()
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                        private var values: List<Children>,
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
            holder.idView.text = item.data.id
            holder.contentView.text = item.data.title

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        fun update(data:List<Children>){
            values = data
            notifyDataSetChanged()
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
