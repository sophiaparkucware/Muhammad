package com.bandapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bandapp.R
import com.bandapp.adapter.BandsAdapter
import com.bandapp.adapter.InvitesAdapter
import com.bandapp.database_reference.DatabaseReference
import com.bandapp.models.Band
import com.bandapp.models.BandMember
import com.bandapp.models.User
import com.bandapp.models.UserBand
import com.bandapp.utilities.Utils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_home_screen.*


class HomeScreen : BasicAppCompatActivity() {
    var loggedInUser: User? = null
    lateinit var bandsAdapter: BandsAdapter
    var bandsList: ArrayList<Band?>? = ArrayList<Band?>()
    lateinit var invitesAdapter: InvitesAdapter
    var invitesList: ArrayList<Band>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun initViews() {

    }

    override fun initValues() {
        loggedInUser = intent?.getSerializableExtra("user") as? User
        bandsAdapter = BandsAdapter(this, bandsList!!, { band: Band -> clickedBand(band) })
        invitesAdapter = InvitesAdapter(this, invitesList, { band -> clickedInvite(band) })
        RV_home?.adapter = bandsAdapter
        RV_home?.layoutManager = GridLayoutManager(this, 2)
        RV_Invites?.adapter = invitesAdapter
        RV_Invites?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun initValuesInViews() {
        loadBands()
        fetchInvites()
    }

    override fun setOnViewClickListener() {
        createBandCard?.setOnClickListener {
            val intent = Intent(this, CreateBandScreen::class.java)
            intent.putExtra("user", loggedInUser)
            intent.putExtra("bandsList", bandsList)
            startActivity(intent)
        }

        toolbar_add_band?.setOnClickListener {
            val intent = Intent(this, CreateBandScreen::class.java)
            intent.putExtra("user", loggedInUser)
            intent.putExtra("bandsList", bandsList)
            startActivityForResult(intent, 11)
        }
    }

    private fun loadBands() {
        if (loggedInUser != null) {
            progressBarHome?.visibility = View.VISIBLE
            loggedInUser?.fetchBandsList(success = { list ->
                if (list.size > 0) {
                    bandsList = list
                    yourBandsText?.visibility = View.VISIBLE
                    createBandCard?.visibility = View.GONE
                    progressBarHome?.visibility = View.GONE
                    bandsAdapter.updateList(bandsList!!)

                } else {
                    yourBandsText?.visibility = View.GONE
                    progressBarHome?.visibility = View.GONE
                    createBandCard?.visibility = View.VISIBLE
                }
            }, failure = { s ->
                yourBandsText?.visibility = View.GONE
                progressBarHome?.visibility = View.GONE
                Utils.showSnackbar(progressBarHome, s!!, Color.RED)
            })
        }
    }

    fun clickedBand(band: Band) {
        val intent = Intent(this, BandDetailScreen::class.java)
        intent.putExtra("band", band)
        startActivity(intent)
    }

    fun clickedInvite(band: Band?) {
        Utils.createAlert(this, resources.getString(R.string.invite_message), event = { b ->
            if (b) {
                acceptInvite(band)
            } else {
                if (loggedInUser != null) {
                    val invites = loggedInUser?.invites
                    if (invites != null && invites.size > 0) {
                        invites.remove(band?.bandId)
                        DatabaseReference.getUsersReference().child(loggedInUser?.id!!).child("invites")
                            .setValue(invites)
                        if (invites.size > 0) {
                            invitesList?.remove(band)
                            invitesAdapter.updateList(invitesList)
                        } else {
                            invitesLayout?.visibility = View.GONE
                        }
                    }
                }
            }
        })


    }

    private fun fetchInvites() {
        if (loggedInUser != null) {
            if (loggedInUser?.invites != null && loggedInUser?.invites?.size!! > 0) {
                for (i in loggedInUser?.invites!!) {
                    fetchBandWithId(i)
                }
            } else {
                invitesLayout?.visibility = View.GONE
                progressBar_Invites?.visibility = View.GONE
            }
        } else {
            invitesLayout?.visibility = View.GONE
            progressBar_Invites?.visibility = View.GONE
        }
    }

    private fun fetchBandWithId(id: String) {
        DatabaseReference.getBandsReference().child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Utils.showSnackbar(progressBarHome, "error occured", Color.RED)
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists() && p0.getValue() != null) {
                    val band = p0.getValue(Band::class.java)
                    if (band != null) {
                        band.bandId = p0.key
                        invitesList?.add(band)
                    }
                    if (invitesList?.size == loggedInUser?.invites?.size) {
                        invitesLayout?.visibility = View.VISIBLE
                        progressBar_Invites?.visibility = View.GONE
                        invitesAdapter.updateList(invitesList)
                    }
                } else {

                }
            }
        })
    }

    private fun acceptInvite(band: Band?) {
        val membersList = band?.membersList
        if (membersList != null) {
            progressBar_Invites?.visibility = View.VISIBLE
            val bandMember = BandMember()
            bandMember.memberId = loggedInUser?.id
            bandMember.username = loggedInUser?.username
            bandMember.profilePicture = loggedInUser?.profilePicture
            membersList.add(bandMember)
            DatabaseReference.getBandsReference().child(band.bandId!!).child("membersList").setValue(membersList)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        addBandToUserBands(band)

                    } else {
                        progressBar_Invites?.visibility = View.GONE
                        Utils.showSnackbar(progressBarHome, "cannot accept invite", Color.RED)
                    }
                }.addOnFailureListener {
                    progressBar_Invites?.visibility = View.GONE
                    Utils.showSnackbar(progressBarHome, "cannot accept invite", Color.RED)
                }

        }
    }

    private fun addBandToUserBands(band: Band?) {
        if (loggedInUser != null) {
            var userbands = loggedInUser?.userBands
            val userBand = UserBand()
            userBand.id = band?.bandId
            userBand.isAdmin = false
            if (userbands != null) {
                userbands.add(userBand)
            } else {
                userbands = ArrayList()
                userbands.add(userBand)
            }
            DatabaseReference.getUsersReference().child(loggedInUser?.id!!).child("userBands").setValue(userbands)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val invites = loggedInUser?.invites
                        if (invites != null) {
                            invites.remove(band?.bandId!!)
                            DatabaseReference.getUsersReference().child(loggedInUser?.id!!).child("invites")
                                .setValue(invites).addOnCompleteListener { task1 ->
                                    progressBar_Invites?.visibility = View.GONE
                                    if (task1.isSuccessful) {
                                        invitesList?.remove(band)
                                        if (invitesList?.size!! > 0) {
                                            invitesAdapter.updateList(invitesList)
                                        } else {
                                            invitesLayout?.visibility = View.GONE
                                        }
                                    } else {
                                        progressBar_Invites?.visibility = View.GONE
                                        Utils.showSnackbar(progressBarHome, "cannot accept invite", Color.RED)
                                    }
                                }.addOnFailureListener {
                                    progressBar_Invites?.visibility = View.GONE
                                    Utils.showSnackbar(progressBarHome, "cannot accept invite", Color.RED)
                                }

                        }
                    } else {
                        Utils.showSnackbar(progressBarHome, "cannot accept invite", Color.RED)
                    }
                }.addOnFailureListener {
                    Utils.showSnackbar(progressBarHome, "cannot accept invite", Color.RED)
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            11 -> {
                val user = data?.getSerializableExtra("user") as User?
                if (user != null) {
                    loggedInUser = user
                }
            }
        }
    }

}
