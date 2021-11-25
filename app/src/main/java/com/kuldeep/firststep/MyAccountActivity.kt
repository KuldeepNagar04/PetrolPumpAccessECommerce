package com.kuldeep.firststep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_myaccount.*


class MyAccountActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myaccount)



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")


        loadProfile()

        editProfileButton.setOnClickListener {
            startActivity(Intent(this@MyAccountActivity, SetProfileActivity::class.java))
        }

        addressBookLayout.setOnClickListener {
            Log.d("HHHHHHHHHH","1111111111")
            startActivity(Intent(this@MyAccountActivity, AddressBookActivity::class.java))
        }

        logoutLaout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadProfile() {

        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        //emailText.text = "Email  -- > "+user?.email

        userreference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                phoneNumberText.text = "+91"+snapshot.child("phoneNumber").value.toString()
                firstnameText.text ="Hello, "+snapshot.child("firstname").value.toString()
                emailText.text = snapshot.child("email").value.toString()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

}