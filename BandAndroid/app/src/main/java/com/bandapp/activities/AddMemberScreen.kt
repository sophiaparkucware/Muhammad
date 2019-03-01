package com.bandapp.activities

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.bandapp.R
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.Band
import com.bandapp.models.User
import com.bandapp.utilities.Utils
import kotlinx.android.synthetic.main.activity_add_member_screen.*

class AddMemberScreen : BasicAppCompatActivity() {
    var band: Band? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member_screen)
    }

    override fun initViews() {

    }

    override fun initValues() {
        band = intent?.getSerializableExtra("band") as Band?
    }

    override fun initValuesInViews() {
        input_member_username?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0?.toString()?.trim()?.length!! > 0) {
                    doneTextAddMember?.isEnabled = true
                    doneTextAddMember?.textSize = 17f
                    doneTextAddMember?.setTextColor(Color.BLACK)
                    return
                }
                doneTextAddMember?.isEnabled = false
                doneTextAddMember?.textSize = 14f
                doneTextAddMember.setTextColor(Color.parseColor("#99AAAAAA"))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })

    }

    override fun setOnViewClickListener() {
        doneTextAddMember?.setOnClickListener {
            checkIfSearchedUserExists()
        }

        backImageAddMember?.setOnClickListener {
            finish()
        }
    }

    private fun checkIfSearchedUserExists() {
        progressBar_add_member?.visibility = View.VISIBLE
        User().fetchSearchedUser(input_member_username?.text?.toString(), success = { it ->
            progressBar_add_member?.visibility = View.GONE
            if (it != null) {
                if (!it.id?.equals(DatabaseReference.getFirebaseAuth().currentUser?.uid)!!) {
                    sendInviteToSearchedUser(it)
                    Utils.showSnackbar(input_member_username, "Invite sent", Color.GREEN)
//                    finish()
                    input_member_username?.setText("")
                } else {
                    Utils.showSnackbar(
                        input_member_username,
                        "It's you :)",
                        Color.RED
                    )
                }
            } else {
                Utils.showSnackbar(input_member_username, "No user found", Color.RED)
            }
        }, failure = { it ->
            progressBar_add_member?.visibility = View.GONE
            Utils.showSnackbar(input_member_username, it!!, Color.RED)
        })
    }

    private fun sendInviteToSearchedUser(user: User?) {
        var invitesList: ArrayList<String>? = user?.invites
        if (invitesList != null) {
            invitesList.add(band?.bandId!!)
        } else {
            invitesList = ArrayList<String>()
            invitesList.add(band?.bandId!!)
        }
        DatabaseReference.getUsersReference().child(user?.id!!).child("invites").setValue(invitesList)
    }
}
