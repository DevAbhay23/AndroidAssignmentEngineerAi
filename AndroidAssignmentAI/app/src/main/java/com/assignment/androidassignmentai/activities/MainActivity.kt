package com.assignment.androidassignmentai.activities

import android.app.ListActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.assignment.androidassignmentai.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navigate to Posts List Activity //

        button_fetch_posts.setOnClickListener {
            val listIntent=Intent(this,PostsListActivity::class.java)
            startActivity(listIntent)
        }
    }
}
