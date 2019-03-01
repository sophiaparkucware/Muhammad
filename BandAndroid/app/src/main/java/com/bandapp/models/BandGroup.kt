package com.bandapp.models

import java.io.Serializable

class BandGroup: Serializable {
    var memberIds: ArrayList<String>? = ArrayList()
    var name: String? = ""
    var groupId: String? = ""
}