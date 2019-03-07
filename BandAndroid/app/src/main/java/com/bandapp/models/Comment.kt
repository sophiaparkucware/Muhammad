package com.bandapp.models

import com.bandapp.database_reference.DatabaseReference
import java.io.Serializable

open class Comment : Serializable {
    var commentId: String? = ""
    var content: String? = ""
    var createdTime: Long? = null
    var username: String? = ""
    var profileImage: String? = ""
    var userId: String? = ""

    fun postComment(postId: String?) {
        val map: HashMap<String, Any> = HashMap()
        map.put("content", this.content!!)
        map.put("createdTime", this.createdTime!!)
        map.put("username", this.username!!)
        map.put("profileImage", this.profileImage!!)
        map.put("userId", this.userId!!)
        DatabaseReference.getPostsReference().child(postId!!).child("comments").child(this.commentId!!).setValue(map)
    }
}