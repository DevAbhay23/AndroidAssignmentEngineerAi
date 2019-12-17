package com.assignment.androidassignmentai.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.assignment.androidassignmentai.R
import com.assignment.androidassignmentai.adapter.PostsListAdapter
import com.assignment.androidassignmentai.interfaces.Callback
import com.assignment.androidassignmentai.models.Posts
import kotlinx.android.synthetic.main.activity_posts_list.*
import org.json.JSONArray
import org.json.JSONObject

class PostsListActivity : AppCompatActivity() ,Callback {

    val TAG="LISTSACTIVITY"
    var list:ArrayList<Posts> = ArrayList();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_list)
        rv_posts.layoutManager=LinearLayoutManager(this)
        getRecordsFromServer(1)

        pull_refresh.setOnRefreshListener {

            list.clear()
            getRecordsFromServer(1)
            pull_refresh.isRefreshing=false
        }
    }

    fun getRecordsFromServer(pageNo:Int){
        val queue=Volley.newRequestQueue(this)
        val requestUrl="https://hn.algolia.com/api/v1/search_by_date?tags=story&page="+pageNo

        val jsonArrayRequest=JsonObjectRequest(Request.Method.GET,requestUrl,null,
        Response.Listener<JSONObject> {
            response ->  Log.d(TAG,"Api Success "+response.toString()) ;parseNetworkResponse(response)
        },
         Response.ErrorListener {
             error ->  Log.d(TAG,"Api Failure "+error.toString())
         }
        )
        queue.add(jsonArrayRequest)
    }

    fun parseNetworkResponse(response:JSONObject){
        val recordArray:JSONArray=response.getJSONArray("hits")
        val recordsList:ArrayList<Posts> = ArrayList()
        for(i in 0 until recordArray.length()){
            val item=recordArray.getJSONObject((i))
            val modelObj= Posts(item.getString("created_at"),item.getString("title"),item.getString("url"),item.getString("author"))
            recordsList.add(modelObj)
        }
        list=recordsList
        setAdapter(list);
    }

    fun setAdapter(list:ArrayList<Posts>){
        rv_posts.adapter=PostsListAdapter(this,list,this);
    }

    override fun onItemClicked(count:Int) {
        text_selected_posts.text= count.toString();
    }


}
