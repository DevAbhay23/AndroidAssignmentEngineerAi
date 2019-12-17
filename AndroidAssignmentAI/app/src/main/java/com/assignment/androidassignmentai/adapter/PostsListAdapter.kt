package com.assignment.androidassignmentai.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.assignment.androidassignmentai.R
import com.assignment.androidassignmentai.interfaces.Callback
import com.assignment.androidassignmentai.models.Posts
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlin.collections.ArrayList


class PostsListAdapter internal constructor(mContext:Context,mList: ArrayList<Posts>,mCallback:Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val context:Context=mContext
    private var list = mList
    private var selectedPostsCount=0
    private var callback=mCallback
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView=inflater.inflate(R.layout.item_layout,parent,false)
        return NewsListVH(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: NewsListVH = holder as NewsListVH
        viewHolder.bindItems(position)
    }

    inner class NewsListVH(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(position: Int) {
            val postsItems = list[position]
           itemView.text_title.text=postsItems.title
           itemView.text_createdAt.text=postsItems.created_at
            itemView.setOnClickListener {
              if(itemView.toggle_switch.isChecked){
                  itemView.toggle_switch.isChecked=false
                  itemView.setBackgroundColor(Color.WHITE)
                  selectedPostsCount -= 1
              }
                else{
                  itemView.toggle_switch.isChecked=true
                  itemView.setBackgroundColor(Color.GRAY)
                  selectedPostsCount += 1
              }
                callback.onItemClicked(selectedPostsCount)
            }
        }

    }


}
