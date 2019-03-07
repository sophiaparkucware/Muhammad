package com.bandapp.activities

import android.graphics.Color
import android.graphics.Point
import android.graphics.PostProcessor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bandapp.R
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.Band
import com.bandapp.models.Post
import com.bandapp.models.User
import com.bandapp.utilities.Utils
import kotlinx.android.synthetic.main.activity_create_band_screen.*
import kotlinx.android.synthetic.main.activity_create_post_screen.*

class CreatePostScreen : BasicAppCompatActivity() {
    var user: User? = null
    var band: Band? = null
    var post: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post_screen)
    }

    override fun initViews() {

    }

    override fun initValues() {
        user = intent.getSerializableExtra("user") as User?
        band = intent.getSerializableExtra("band") as Band?
        post = intent.getSerializableExtra("post") as Post?
    }

    override fun initValuesInViews() {
        if (post != null) {
            input_post?.setText(post?.content)
            donePost?.isEnabled = true
            donePost?.setTextColor(Color.BLACK)
            donePost?.textSize = 17f
        }
    }

    override fun setOnViewClickListener() {
        backImagePost?.setOnClickListener {
            finish()
        }

        input_post?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.toString()?.trim()?.length!! > 0) {
                    donePost?.isEnabled = true
                    donePost?.setTextColor(Color.BLACK)
                    donePost?.textSize = 17f
                    return
                }
                donePost?.textSize = 14f
                donePost?.isEnabled = false
                donePost.setTextColor(Color.parseColor("#99AAAAAA"))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        donePost?.setOnClickListener {
            if (post == null) {
                createPost()
            } else {
                editPost()
            }
        }
    }

    private fun createPost() {
        if (user != null) {
            progressBar_create_post?.visibility = View.VISIBLE
            val post = Post()
            post.postId = DatabaseReference.getPostsReference().push().key
            post.content = input_post?.text?.toString()
            post.createdTime = System.currentTimeMillis()
            post.profileImage = user?.profilePicture
            post.userId = user?.id
            post.username = user?.username
            post.owner = band?.bandId
            post.createPost { b ->
                if (b) {
                    finish()
                } else {
                    progressBar_create_post?.visibility = View.GONE
                    Utils.showSnackbar(progressBar_create_post, "cannot create post", Color.RED)
                }
            }
        }

    }

    private fun editPost() {
        if (user != null) {
            progressBar_create_post?.visibility = View.VISIBLE
            post?.createdTime = System.currentTimeMillis()
            post?.content = input_post?.text?.toString()
            post?.editPost { b ->
                if (b) {
                    finish()
                } else {
                    progressBar_create_post?.visibility = View.GONE
                    Utils.showSnackbar(progressBar_create_post, "cannot create post", Color.RED)
                }
            }
        }
    }
}
