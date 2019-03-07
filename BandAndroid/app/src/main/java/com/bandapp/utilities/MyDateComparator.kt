package com.bandapp.utilities

import com.bandapp.models.Post
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

class MyDateComparator : Comparator<Post> {

    override fun compare(p0: Post?, p1: Post?): Int {
        val a = p0?.createdTime
        val b = p1?.createdTime
        val c = Calendar.getInstance()
        c.timeInMillis = a!!
        val d = Calendar.getInstance()
        d.timeInMillis = b!!
        if (c.after(d)) {
            return -1
        } else if (c.before(d)) {
            return 1
        } else {
            return 0
        }
    }

}