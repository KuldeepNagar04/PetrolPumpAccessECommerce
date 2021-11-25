package com.kuldeep.firststep

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kuldeep.firststep.Model.Address
import kotlinx.android.synthetic.main.activity_order_placed.nameTv
import kotlinx.android.synthetic.main.activity_order_placed.pinTv
import kotlinx.android.synthetic.main.activity_order_placed.tvLineThree
import kotlinx.android.synthetic.main.activity_order_placed.tvLineTwo

class OrderPlacedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_placed)

        displayPriorAddress()

    }

    private fun displayPriorAddress(){
        val user = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().reference.child("profile").child(user?.uid!!).child("address")

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