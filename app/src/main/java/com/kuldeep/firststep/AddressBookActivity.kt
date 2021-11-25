package com.kuldeep.firststep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kuldeep.firststep.Adapter.AddressAdapter
import com.kuldeep.firststep.Model.Address
import kotlinx.android.synthetic.main.activity_addressbook.*

class AddressBookActivity : AppCompatActivity(){

    private lateinit var databaseReference: DatabaseReference
    private lateinit var addressRecyclerview : RecyclerView
    private lateinit var addressArrayList : ArrayList<Address>
    private lateinit var addressNodeList : ArrayList<String>

    private var lastNode : Int =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addressbook)

        addressRecyclerview = findViewById(R.id.userList)
        addressRecyclerview.layoutManager = LinearLayoutManager(this)
        addressRecyclerview.setHasFixedSize(true)

        addressArrayList = arrayListOf<Address>()
        addressNodeList =  arrayListOf()
        getAddressData()

        addNewAddress.setOnClickListener {
            if(addressNodeList.isEmpty()){
                lastNode = 0
            }else{
                lastNode = addressNodeList.last().toInt()
            }
            Log.d("Last Address Node",lastNode.toString())
            intent = Intent(this, AddNewAddressActivity::class.java)
            intent.putExtra("addressCount",lastNode)
            startActivity(intent)
        }





    }

    private fun getAddressData() {
        Log.d("HHHHHHHHHH","222222222")
        val user = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance()?.reference!!.child("profile").child(user?.uid!!).child("address")
        Log.d("HHHHHHHHHH","33333333333333")
        databaseReference.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    var currAddress : Address? = null

                    for (addressSnapshot in snapshot.children){
                        Log.d("HHHHHHHHHH","44444444")
                        Log.d("jbdfhoie f",addressSnapshot.toString())

                        val address = addressSnapshot.getValue(Address::class.java)
                        if(address!=null){
                            if(address.priority=="1"){
                                currAddress = address
                            }
                        }
                        Log.d("HHHHHHHHHH","5555555555555")
                        addressArrayList.add(address!!)
                        addressNodeList.add(addressSnapshot.key.toString())

                    }
                    Log.d("HHHHHHHHHH","666666666")
                    addressRecyclerview.adapter = AddressAdapter(currAddress!!,addressArrayList,this@AddressBookActivity)
                    Log.d("HHHHHHHHHH","777777777777")

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

    }



}