package com.bandapp.models

import com.bandapp.database_reference.DatabaseReference
import java.io.Serializable

class Band : Serializable {
    var name: String? = ""
    var picture: String? = ""
    var createdBy: String? = ""
    var bandCreatedTime: String? = ""
    var bandId: String? = ""
    var membersList: ArrayList<BandMember>? = ArrayList()
    var groupsList: ArrayList<BandGroup>? = ArrayList()

    fun sendBandToDB(success: (Boolean?) -> Unit) {
        val map = HashMap<String, Any>()
        map.put("name", this.name!!)
        map.put("picture", this.picture!!)
        map.put("createdBy", this.createdBy!!)
        map.put("bandCreatedTime", this.bandCreatedTime!!)
        map.put("membersList", this.membersList!!)
        map.put("groupsList", this.groupsList!!)
        DatabaseReference.getBandsReference().child(this.bandId!!).setValue(map).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                print("upload success")
                success(true)
            }
        }.addOnFailureListener { exception ->
            print(exception.message)
            success(false)
        }
    }
}