package com.kuldeep.firststep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kuldeep.firststep.Adapter.CartAdapter
import com.kuldeep.firststep.Model.Address
import com.kuldeep.firststep.Model.Cart
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var cartRecyclerview : RecyclerView
    private lateinit var cartArrayList : ArrayList<Cart>

    private var amount : Double = 0.0
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        productId = intent.extras?.getString("productToBeDisplayed")

        cartRecyclerview = findViewById(R.id.cart_list)
        cartRecyclerview.layoutManager = LinearLayoutManager(this)
        cartRecyclerview.setHasFixedSize(true)
        cartArrayList = arrayListOf<Cart>()
        cartRecyclerview.adapter = CartAdapter(amount,cartArrayList,this)

        displayPriorAddress()
        displayCart()

        btnChangeAddress.setOnClickListener {
            startActivity(Intent(this, AddressBookActivity::class.java))
        }
        btnProcced.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }
    }

    private fun displayCart() {
        databaseReference = FirebaseDatabase.getInstance()?.reference!!.child("product")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (cartSnapshot in snapshot.children){
                        //Log.d("HHHHHHHHHH","Line11111111")
                        //Log.d("HHHHHHHH",cartSnapshot.toString())

                        val cart = cartSnapshot.getValue(Cart::class.java)
                        //Log.d("HHHHHHHHHH","Line2222222")
                        //Log.d("HHHHHHHHH",cart.toString())
                        if (cart != null) {
                            amount+= cart.price?.toDouble() ?: 0.0
                        }
                        cartArrayList.add(cart!!)

                    }
                    //Log.d("HHHHHHHHHH","Line3333333333")
                    cartRecyclerview.adapter?.notifyDataSetChanged()
                    //Log.d("HHHHHHHHHH","Line444444444")

                    findViewById<TextView>(R.id.amount).text=amount.toString()+"INR"
                    total_price.text = "₹"+(amount-40).toString()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        })
    }

    private fun displayPriorAddress(){
        val user = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance()?.reference!!.child("profile").child(user?.uid!!).child("address")

        databaseReference.addValueEventListener(object : ValueEventListener{
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