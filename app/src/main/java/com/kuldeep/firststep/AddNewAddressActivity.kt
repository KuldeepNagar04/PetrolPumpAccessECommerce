package com.kuldeep.firststep

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_addnewaddress.*

class AddNewAddressActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null
    var addressCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnewaddress)



        addressCount = intent.extras?.getInt("addressCount")!! +1
        Log.d("bnsouf :",addressCount.toString())
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        addAddress()
    }

    private fun addAddress() {

        addAddressButton.setOnClickListener {

            if (TextUtils.isEmpty(fullnameInput.text.toString())) {
                fullnameInput.setError("Please enter full name ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(mobileNoInput.text.toString())) {
                mobileNoInput.setError("Please enter phone number ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(lineOneInput.text.toString())) {
                lineOneInput.setError("Please enter address line 1 ")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(lineTwoInput.text.toString())) {
                lineTwoInput.setError("Please enter address line 2 ")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(cityInput.text.toString())) {
                cityInput.setError("Please enter city name ")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(pinCodeInput.text.toString())) {
                pinCodeInput.setError("Please enter pincode ")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(stateInput.toString())) {
                lineOneInput.setError("Please enter state name ")
                return@setOnClickListener
            }


            val currentUser = auth.currentUser
            val currentUSerDb = databaseReference?.child((currentUser?.uid!!))?.child("address")


            val currentUserAddressRef = currentUSerDb?.child(addressCount.toString())
            currentUserAddressRef?.child("id")?.setValue(addressCount.toString())
            currentUserAddressRef?.child("name")?.setValue(fullnameInput.text.toString())
            currentUserAddressRef?.child("phoneNo")?.setValue(mobileNoInput.text.toString())
            currentUserAddressRef?.child("lineOne")?.setValue(lineOneInput.text.toString())
            currentUserAddressRef?.child("lineTwo")?.setValue(lineTwoInput.text.toString())
            currentUserAddressRef?.child("city")?.setValue(cityInput.text.toString())
            currentUserAddressRef?.child("pinCode")?.setValue(pinCodeInput.text.toString())
            currentUserAddressRef?.child("state")?.setValue(stateInput.selectedItem.toString())


            Toast.makeText(this, "Saved Address Successfully. ", Toast.LENGTH_LONG).show()
            finish()
            startActivity(Intent(this,AddressBookActivity::class.java))

        }

    }

}