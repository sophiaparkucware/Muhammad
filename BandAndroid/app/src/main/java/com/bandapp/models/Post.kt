package com.bandapp.models

import com.bandapp.database_reference.DatabaseReference
import com.bandapp.utilities.MyDateComparator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Post : Serializable {
    var owner: String? = "" // The owner of the post is Band
    var lastComment: Comment? = null
    var content: String? = ""
    var postId: String? = ""
    var createdTime: Long? = null
    var username: String? = ""
    var profileImage: String? = ""
    var userId: String? = ""
    var comments: HashMap<String, Comment>? = HashMap()

    fun fetchPosts(id: String, success: (ArrayList<Post>?) -> Unit, failure: (String) -> Unit) {
        DatabaseReference.getPostsReference().orderByChild("owner").equalTo(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    failure("error occured")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.getValue() != null) {
                        val list: ArrayList<Post>? = ArrayList<Post>()
                        for (datasnaphot in p0.children) {
                            val post: Post? = datasnaphot.getValue(Post::class.java)
                            if (post != null) {
                                post.postId = datasnaphot.key
                                list?.add(post)
                            }
                        }
                        Collections.sort(list, MyDateComparator())
                        success(list)
                    } else {
                        success(ArrayList())
                    }
                }

            })
    }

    fun createPost(success: (Boolean) -> Unit) {
        val map: HashMap<String, Any> = HashMap()
        map.put("owner", this.owner!!)
        map.put("content", this.content!!)
        map.put("createdTime", this.createdTime!!)
        map.put("username", this.username!!)
        map.put("profileImage", this.profileImage!!)
        map.put("userId", this.userId!!)
        DatabaseReference.getPostsReference().child(this.postId!!).setValue(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                success(true)
            } else {
                success(false)
            }
        }.addOnFailureListener { exception ->
            success(false)
        }
    }

    fun editPost(success: (Boolean) -> Unit) {
        val map: HashMap<String, Any> = HashMap()
        map.put("content", this.content!!)
        map.put("createdTime", this.createdTime!!)
        DatabaseReference.getPostsReference().child(this.postId!!).updateChildren(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                success(true)
            } else {
                success(false)
            }
        }.addOnFailureListener { exception ->
            success(false)
        }
    }

    fun deletePost(success: (Boolean) -> Unit) {
        DatabaseReference.getPostsReference().child(this.postId!!).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                success(true)
            } else {
                success(false)
            }
        }.addOnFailureListener {
            success(false)
        }
    }

    fun fetchCommentsOfPost(success: (ArrayList<Comment>?) -> Unit, failure: (String) -> Unit) {
        DatabaseReference.getPostsReference().child(this.postId!!).child("comments")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    failure("error occured while fetching comments")
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.getValue() != null) {
                        val list: ArrayList<Comment>? = ArrayList<Comment>()
                        for (datasnapshot in p0.children) {
                            val comment: Comment? = datasnapshot.getValue(Comment::class.java)
                            if (comment != null) {
                                comment.commentId = datasnapshot.key
                                list?.add(comment)
                            }
                        }
                        success(list)
                    } else {
                        success(ArrayList())
                    }
                }

            })
    }

    fun updateLastCommentOfPost() {
        val map: HashMap<String, Any> = HashMap()
        map.put("lastComment", this.lastComment!!)
        DatabaseReference.getPostsReference().child(this.postId!!).updateChildren(map)
    }
}