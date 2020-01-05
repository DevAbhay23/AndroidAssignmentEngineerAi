package com.assignment.androidassignmentai.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.assignment.androidassignmentai.R
import com.assignment.androidassignmentai.adapter.PostsListAdapter
import com.assignment.androidassignmentai.interfaces.Callback
import com.assignment.androidassignmentai.interfaces.OnLoadMoreListener
import com.assignment.androidassignmentai.models.Posts
import com.assignment.androidassignmentai.network.Service.Companion.BASE_URL
import com.assignment.androidassignmentai.network.Service.Companion.GET_STORY_BY_DATE
import com.demo.kotlinmyapp.views.ProgressDialog
import kotlinx.android.synthetic.main.activity_posts_list.*
import org.json.JSONArray
import org.json.JSONObject


class PostsListActivity : AppCompatActivity(), Callback {

    private val TAG = PostsListActivity::class.java.name
    var postsList: ArrayList<Posts> = ArrayList();
    private var recyclerAdapter: PostsListAdapter? = null
    var isScrollFinished = false
    var pageNum = 1
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_list)
        progressDialog = ProgressDialog(this);

        rv_posts.layoutManager = LinearLayoutManager(this)

        pull_refresh.setOnRefreshListener {
            pageNum = 1
            getRecordsFromServer()
            pull_refresh.isRefreshing = false
            onItemClicked(0)
        }
        getRecordsFromServer()
    }

    fun getRecordsFromServer() {
        progressDialog.showDialog()
        val queue = Volley.newRequestQueue(this)
        val requestUrl = BASE_URL + GET_STORY_BY_DATE + pageNum

        val jsonArrayRequest = JsonObjectRequest(Request.Method.GET, requestUrl, null,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "Api Success $response");parseNetworkResponse(response)
            },
            Response.ErrorListener { error ->
                Log.d(TAG, "Api Failure $error"); progressDialog.hideDialog()
            }
        )
        queue.add(jsonArrayRequest)
    }

    private fun parseNetworkResponse(response: JSONObject) {
        val recordArray: JSONArray = response.getJSONArray("hits")
        val recordsList: ArrayList<Posts> = ArrayList()
        for (i in 0 until recordArray.length()) {
            val item = recordArray.getJSONObject((i))
            val modelObj = Posts(
                item.getString("created_at"),
                item.getString("title"),
                item.getString("url"),
                item.getString("author")
            )
            recordsList.add(modelObj)
        }
        setAdapter(recordsList)
    }

    @SuppressLint("ShowToast")
    fun setAdapter(list: ArrayList<Posts>) {
        progressDialog.hideDialog()
        if (pageNum == 1) {
            postsList.clear()
            postsList.addAll(list)
        } else {
            postsList.addAll(postsList.size, list)
        }

        if (list.size <= 0) {
            Toast.makeText(this, "No more records! ", Toast.LENGTH_LONG)
        }

        if (pageNum != 1 && recyclerAdapter != null) {
            recyclerAdapter!!.notifyDataSetChanged()
        } else {
            recyclerAdapter = PostsListAdapter(this, postsList, nested_scrollview, this);
            rv_posts.adapter = recyclerAdapter
            recyclerAdapter!!.setOnLoadMoreListener(MyOnLoadListener())
        }
    }

    override fun onItemClicked(count: Int) {
        val actionBar = supportActionBar
        actionBar!!.title = "Selected items $count"
    }

    inner class MyOnLoadListener : OnLoadMoreListener {
        override fun onLoadMore() {
            if (!isScrollFinished) {
                pageNum++
                getRecordsFromServer()
            }
        }
    }


}
