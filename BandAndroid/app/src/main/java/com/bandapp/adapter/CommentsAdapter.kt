package com.bandapp.adapter

import android.support.v7.view.menu.MenuView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bandapp.R
import com.bandapp.models.Comment
import com.bandapp.utilities.ImageLoaderUtils
import com.bandapp.utilities.Utils
import de.hdodenhof.circleimageview.CircleImageView

class CommentsAdapter(val comments1: ArrayList<Comment>?) : RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {
    var comments = comments1

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val profileImage: CircleImageView = itemView?.findViewById(R.id.profileImage_comment)!!
        val username: TextView = itemView?.findViewById(R.id.username_comment)!!
        val commentText: TextView = itemView?.findViewById(R.id.comment_content)!!
        val commentTime: TextView = itemView?.findViewById(R.id.commentTime_comment)!!
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CommentsAdapter.MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.single_comment, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return comments?.size!!
    }

    override fun onBindViewHolder(p0: CommentsAdapter.MyViewHolder, p1: Int) {
        val comment = comments?.get(p1)
        if (comment != null) {
            ImageLoaderUtils.loadImage(comment.profileImage, p0.profileImage, success = {})
            p0.username.text = comment.username
            p0.commentText.text = comment.content
            p0.commentTime.text = Utils.getTime(comment.createdTime)
        }
    }

    fun updateList(arrayList: ArrayList<Comment>?) {
        this.comments = arrayList
        notifyDataSetChanged()
    }
}