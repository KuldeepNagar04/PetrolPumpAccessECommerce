package com.kuldeep.firststep


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_setprofile.*

class SetProfileActivity : AppCompatActivity(){

    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setprofile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        setProfile()
    }

    private fun setProfile() {


        registerButton.setOnClickListener {

            if(TextUtils.isEmpty(firstnameInput.text.toString())) {
                firstnameInput.setError("Please enter first name ")
                return@setOnClickListener
            } else if(TextUtils.isEmpty(lastnameInput.text.toString())) {
                firstnameInput.setError("Please enter last name ")
                return@setOnClickListener
            }else if(TextUtils.isEmpty(emailInput.text.toString())) {
                firstnameInput.setError("Please enter email ")
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
            currentUSerDb?.child("firstname")?.setValue(firstnameInput.text.toString())
            currentUSerDb?.child("lastname")?.setValue(lastnameInput.text.toString())
            currentUSerDb?.child("email")?.setValue(emailInput.text.toString())

            Toast.makeText(this, "Saved Details Successfully. ", Toast.LENGTH_LONG).show()
            finish()
            startActivity(Intent(this, MyAccountActivity::class.java))

        }
    }
}