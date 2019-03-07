package com.bandapp.activities

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bandapp.R
import com.bandapp.models.Band
import com.bandapp.utilities.ImageLoaderUtils
import kotlinx.android.synthetic.main.activity_band_detail_screen.*
import android.support.design.widget.AppBarLayout

import android.view.WindowManager
import android.os.Build

import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import com.bandapp.adapter.BandGroupsAdapter
import com.bandapp.adapter.PostsAdapter
import com.bandapp.models.BandGroup
import com.bandapp.models.Post
import com.bandapp.models.User
import com.bandapp.utilities.Dialogs
import com.bandapp.utilities.Utils
import kotlinx.android.synthetic.main.single_post.*
import android.widget.Toast
import android.view.MenuItem


class BandDetailScreen : BasicAppCompatActivity() {
    var band: Band? = null
    //    lateinit var groupsAdapter: BandGroupsAdapter
//    var groupsList: ArrayList<BandGroup>? = ArrayList()
    var posts: ArrayList<Post>? = ArrayList()
    lateinit var postsAdapter: PostsAdapter
    var user: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.bandapp.R.layout.activity_band_detail_screen)
    }

    override fun initViews() {
    }

    override fun initValues() {
        band = intent.getSerializableExtra("band") as Band?
        user = intent.getSerializableExtra("user") as User?
//        groupsAdapter = BandGroupsAdapter(this, groupsList, { bandGroup -> clickedBandGroup(bandGroup) })
//        RV_BandGroups?.layoutManager = LinearLayoutManager(this)
//        RV_BandGroups?.adapter = groupsAdapter

        postsAdapter = PostsAdapter(
            this,
            band,
            user,
            posts,
            { post, toComment -> postToEditDel(post, toComment) }, progressBar_posts)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RV_Posts?.layoutManager = layoutManager
        RV_Posts?.adapter = postsAdapter
    }


    override fun initValuesInViews() {
        ImageLoaderUtils.loadImage(band?.picture, band_image, success = {})
        bandName?.setText(band?.name)
        val noOfmembers = band?.membersList?.size
        if (noOfmembers!! > 1) {
            noOfMembers?.setText(noOfmembers.toString() + " Members")
        } else {
            noOfMembers?.setText(noOfmembers.toString() + " Member")
        }
//        groupsList = band?.groupsList
//        groupsAdapter.updateList(groupsList)
        fetchPosts()
    }

    override fun setOnViewClickListener() {
        backImageBandDetail?.setOnClickListener {
            finish()
        }

        post?.setOnClickListener {
            val intent = Intent(this, CreatePostScreen::class.java)
            intent.putExtra("user", user)
            intent.putExtra("band", band)
            startActivity(intent)
        }

        inviteLayout?.setOnClickListener {
            val intent = Intent(this, AddMemberScreen::class.java)
            intent.putExtra("user", user)
            intent.putExtra("band", band)
            startActivity(intent)
        }

    }

    fun clickedBandGroup(bandGroup: BandGroup) {
//        val intent = Intent(this, BandDetailScreen::class.java)
//        intent.putExtra("band", band)
//        startActivity(intent)
    }

    private fun postToEditDel(post: Post, toComment: Boolean) {
        if (!toComment) {
//            Dialogs.createPopup(post, this, progressBar_posts)
//            Dialogs.editDelPostDialog(this@BandDetailScreen, edit = { b ->
//                if (b) {
//                    // edit post
//                    val intent = Intent(this, CreatePostScreen::class.java)
//                    intent.putExtra("user", user)
//                    intent.putExtra("band", band)
//                    intent.putExtra("post", post)
//                    startActivity(intent)
//                } else {
//                    // delete post
//                    post.deletePost(success = { b1 ->
//                        if (b1) {
//                            Utils.showSnackbar(
//                                progressBar_posts,
//                                "post deleted",
//                                resources.getColor(R.color.colorPrimary)
//                            )
//                        } else {
//                            Utils.showSnackbar(progressBar_posts, "cannot delete post", Color.RED)
//                        }
//                    })
//                }
//            })
        } else {
            val intent = Intent(this, CommentsScreen::class.java)
            intent.putExtra("user", user)
            intent.putExtra("band", band)
            intent.putExtra("post", post)
            startActivity(intent)
        }
    }

    private fun fetchPosts() {
        progressBar_posts?.visibility = View.VISIBLE
        Post().fetchPosts(band?.bandId!!, success = { arrayList ->
            progressBar_posts?.visibility = View.GONE
            if (arrayList != null && arrayList.size > 0) {
                RV_Posts?.visibility = View.VISIBLE
                postsHint?.visibility = View.INVISIBLE
                postsAdapter.updateList(arrayList)
            } else {
                postsHint?.visibility = View.VISIBLE
                RV_Posts?.visibility = View.INVISIBLE
            }
        }, failure = { s ->
            progressBar_posts?.visibility = View.GONE
            postsHint?.visibility = View.INVISIBLE
        })
    }
}
