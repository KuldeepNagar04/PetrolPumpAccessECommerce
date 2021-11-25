package com.kuldeep.firststep

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kuldeep.firststep.Model.Address
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_payment.nameTv
import kotlinx.android.synthetic.main.activity_payment.pinTv
import kotlinx.android.synthetic.main.activity_payment.tvLineThree
import kotlinx.android.synthetic.main.activity_payment.tvLineTwo

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        displayPriorAddress()

        checkout.setOnClickListener {
            startActivity(Intent(this, OrderPlacedActivity::class.java))
        }

    }

    private fun displayPriorAddress(){
        val user = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance()?.reference!!.child("profile").child(user?.uid!!).child("address")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (addressSnapshot in snapshot.children){
                        val address = addressSnapshot.getValue(Address::class.java)
                        if (address != null) {
                            if(address.priority?.toInt() ==1){
                                nameTv.text = address.name+","
                                pinTv.text = address.pinCode
                                tvLineTwo.text = address.lineOne+", "+address.lineTwo
                                tvLineThree.text =address.city+", "+address.state

                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        } )

    }
}