package com.bandapp.activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BlockedNumberContract
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.bandapp.R
import com.bandapp.adapter.CommentsAdapter
import com.bandapp.models.Band
import com.bandapp.models.Comment
import com.bandapp.models.Post
import com.bandapp.models.User
import com.bandapp.utilities.ImageLoaderUtils
import com.bandapp.utilities.Utils
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_comments_screen.*
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList


class CommentsScreen : BasicAppCompatActivity() {
    var band: Band? = null
    var post: Post? = null
    var user: User? = null
    lateinit var commentsAdapter: CommentsAdapter
    var comments: ArrayList<Comment>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments_screen)
    }

    override fun initViews() {

    }

    override fun initValues() {
        band = intent.getSerializableExtra("band") as Band?
        post = intent.getSerializableExtra("post") as Post?
        user = intent.getSerializableExtra("user") as User?
        toolbar_comments?.setTitle(band?.name)
        setSupportActionBar(toolbar_comments)
        commentsAdapter = CommentsAdapter(comments)
        RV_comments?.layoutManager = LinearLayoutManager(this)

        RV_comments?.adapter = commentsAdapter
        fetchCommentsOfPost()
    }

    override fun initValuesInViews() {
        val formatter = SimpleDateFormat("MMMM d, yyyy hh:mm a")
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(post?.createdTime!!)
        ImageLoaderUtils.loadImage(post?.profileImage, profileImage_comment_screen, success = {})
        username_comment_screen?.setText(post?.username)
        postTime_comment_screen?.setText(formatter.format(calendar.time))
        post_content_comment_screen?.setText(post?.content)
    }

    override fun setOnViewClickListener() {
        backImageComments?.setOnClickListener {
            finish()
        }

        sendCommentImage?.setOnClickListener {
            if (input_comment?.text?.toString()?.trim()?.length!! > 0) {
                postCommentToPost()
            }
        }

    }

    private fun fetchCommentsOfPost() {
        post?.fetchCommentsOfPost(success = { arrayList ->
            if (arrayList != null && arrayList.size > 0) {
                commentsNum?.text = arrayList?.size.toString() + " comments"
                comments = arrayList
                commentsAdapter.updateList(comments)
                RV_comments?.scrollToPosition(comments?.size!! - 1)
            } else {
                commentsNum?.text = "0 comments"
            }

        }, failure = { s ->
            Utils.showSnackbar(messageLayout, s, Color.RED)
        })
    }

    private fun postCommentToPost() {
        val comment = Comment()
        comment.content = input_comment?.text?.toString()
        comment.commentId = FirebaseDatabase.getInstance().reference.push().key
        comment.createdTime = System.currentTimeMillis()
        comment.userId = user?.id
        comment.username = user?.username
        comment.profileImage = user?.profilePicture
        comment.postComment(post?.postId!!)
        post?.lastComment = comment
        post?.updateLastCommentOfPost()
        comments?.add(comment)
        commentsAdapter.updateList(comments)
        RV_comments?.scrollToPosition(comments?.size!! - 1)
        input_comment?.setText("")
    }
}
