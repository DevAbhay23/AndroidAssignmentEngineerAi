package com.assignment.androidassignmentai.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.androidassignmentai.R
import com.assignment.androidassignmentai.interfaces.Callback
import com.assignment.androidassignmentai.interfaces.OnLoadMoreListener
import com.assignment.androidassignmentai.models.Posts
import kotlinx.android.synthetic.main.recycl_item_layout.view.*
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class PostsListAdapter internal constructor(mContext:Context,mList: ArrayList<Posts>,nestedScrollView:NestedScrollView,mCallback:Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var context:Context = mContext
    var list:ArrayList<Posts> = mList
    var selectedPostsCount:Int = 0
    var callback:Callback = mCallback
    private lateinit var onLoadMoreListener:OnLoadMoreListener

    init{
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val view: View =
                nestedScrollView.getChildAt(nestedScrollView.childCount - 1)
            val diff: Int =
                view.bottom - (nestedScrollView.height + nestedScrollView.scrollY)
            if (diff == 0) {
                onLoadMoreListener.onLoadMore()
            }
        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val itemView=inflater.inflate(R.layout.recycl_item_layout,parent,false)
        return PostsListVH(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: PostsListVH = holder as PostsListVH
        viewHolder.itemView.tag=position
        viewHolder.bindItems()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class PostsListVH(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems() {
            val position=adapterPosition
            val postsItems = list[position]
           itemView.text_title.text=postsItems.title
           itemView.text_createdAt.text=getConvertedDate(postsItems.created_at)

            itemView.toggle_switch.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    itemView.setBackgroundColor(Color.LTGRAY)
                    selectedPostsCount += 1
                } else {
                    itemView.background=null
                    selectedPostsCount -= 1
                }
                if(selectedPostsCount<0){
                    selectedPostsCount=0
                }
                callback.onItemClicked(selectedPostsCount)
            }

//            itemView.setOnClickListener {
//              val mPosition = it.tag as Int
//              if(itemView.toggle_switch.isChecked){
//                  itemView.toggle_switch.isChecked=false
//                  itemView.background=null
//                  selectedPostsCount -= 1
//              }
//                else{
//                  itemView.toggle_switch.isChecked=true
//                  itemView.setBackgroundColor(Color.GRAY)
//                  selectedPostsCount += 1
//              }
//
//            }
        }

    }

    fun getConvertedDate(dateString:String):String{
        return ZonedDateTime.parse(dateString).format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm"))
     }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener){
        this.onLoadMoreListener=mOnLoadMoreListener
    }

}
