package com.bandapp.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bandapp.R
import com.bandapp.models.*
import com.bandapp.utilities.Dialogs
import com.bandapp.utilities.ImageLoaderUtils
import com.bandapp.utilities.Utils
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.TimeUnit


class PostsAdapter(
    activity1: Activity, band1: Band?, user1: User?,
    postsList: ArrayList<Post>?,
    val clickListenerEditDelPost: (Post, Boolean) -> Unit, progressBar1: ProgressBar
) : RecyclerView.Adapter<PostsAdapter.MyViewHolder>() {
    var posts = postsList
    val user = user1
    val activity = activity1
    val progressBar = progressBar1
    val band = band1

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val usernamePost: TextView = itemView?.findViewById(R.id.username_single_post)!!
        val postTime: TextView = itemView?.findViewById(R.id.postTime)!!
        val postContent: TextView = itemView?.findViewById(R.id.post_content)!!
        //        val commentLayout: LinearLayout = itemView?.findViewById(R.id.commentlayout)!!
        val profileImagePost: CircleImageView = itemView?.findViewById(R.id.profileImage_single_post)!!
        val profileImageComment: CircleImageView = itemView?.findViewById(R.id.profileImage_comment)!!
        val usernameComment: TextView = itemView?.findViewById(R.id.username_single_comment)!!
        val commentTime: TextView = itemView?.findViewById(R.id.commentTime)!!
        val commentText: TextView = itemView?.findViewById(R.id.commentText)!!
        val popupLayout: ImageView = itemView?.findViewById(R.id.editDelPostIcon)!!
        val postParentLayout: LinearLayout = itemView?.findViewById(R.id.postParentLayout)!!
        val commentLayout: LinearLayout = itemView?.findViewById(R.id.commentlayout_post)!!

        fun bind(
            post: Post,
            activity1: Activity,
            progressBar1: ProgressBar,
            clickListenerEditDelPost: (Post, Boolean) -> Unit
        ) {
            popupLayout.setOnClickListener {
                //                clickListenerEditDelPost(post, false)
                Dialogs.createPopup(post, activity1, popupLayout, progressBar1)
            }
        }

        fun comment(post: Post, clickListenerEditDelPost: (Post, Boolean) -> Unit) {
            postParentLayout.setOnClickListener { clickListenerEditDelPost(post, true) }
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostsAdapter.MyViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.single_post, p0, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts?.size!!
    }

    override fun onBindViewHolder(p0: PostsAdapter.MyViewHolder, p1: Int) {
        val post = posts?.get(p1)
        if (post != null) {
            p0.usernamePost.text = post.username!!
            p0.postTime.text = Utils.getTime(post.createdTime!!)
            p0.postContent.text = post.content
            ImageLoaderUtils.loadImage(post.profileImage, p0.profileImagePost, success = {})
//
            p0.bind(post, activity, progressBar, clickListenerEditDelPost)
            p0.comment(post, clickListenerEditDelPost)
            if (post.userId?.equals(user?.id)!! || band?.createdBy?.equals(user?.id)!!) {
                p0.popupLayout.visibility = View.VISIBLE
            } else {
                p0.popupLayout.visibility = View.GONE
            }

//            val map = post.comments
//            if (map != null && map.size > 0) {
            val comment: Comment? = post.lastComment
            if (comment != null) {
                p0.commentLayout.visibility = View.VISIBLE
                p0.profileImageComment.visibility = View.VISIBLE
                ImageLoaderUtils.loadImage(comment.profileImage, p0.profileImageComment, success = {})
                p0.usernameComment.text = comment.username
                p0.commentText.text = comment.content
                p0.commentTime.text = Utils.getTime(comment.createdTime)
            } else {
                p0.commentLayout.visibility = View.GONE
                p0.profileImageComment.visibility = View.GONE
            }
//            }
        }
    }

    fun updateList(posts: ArrayList<Post>?) {
        this.posts = posts
        notifyDataSetChanged()
    }
}