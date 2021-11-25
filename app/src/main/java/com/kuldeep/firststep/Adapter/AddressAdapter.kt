package com.kuldeep.firststep.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kuldeep.firststep.Model.Address
import com.kuldeep.firststep.R
import java.util.*
import kotlin.collections.ArrayList


class AddressAdapter(private val oldPriorAddressRefrence : Address, private val addressList : ArrayList<Address>, private val context : Context) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.user_address,
            parent,false)
        return AddressViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        val currentitem = addressList[position]

        holder.id.text = currentitem.id
        holder.name.text = currentitem.name
        holder.lineOne.text = currentitem.lineOne
        holder.lineTwo.text = currentitem.lineTwo
        holder.city.text = currentitem.city
        holder.state.text = currentitem.state
        holder.pinCode.text = currentitem.pinCode
        holder.phoneNo.text = currentitem.phoneNo

        if(currentitem.priority?.toInt() == 1){
            holder.selectedAddess.setBackgroundResource(R.drawable.icon_radio_button_checked)
        }

//        holder.selectedAddess.setOnClickListener (object : View.OnClickListener {
//            override fun onClick(view: View?) {
//                Log.d("HHHHHHHHHH","333333333333334")
//                val clickedAddress = holder.id.text.toString()
//
//                val map : HashMap<String, Any> = HashMap<String, Any>()
//                map.put("priority","0")
//
//                val map0 : HashMap<String, Any> = HashMap<String, Any>()
//                map.put("priority","1")
//                Log.d("HHHHHHHHHH","333333333333334")
//                val user = FirebaseAuth.getInstance().currentUser
//                val databaseReference = FirebaseDatabase.getInstance()?.reference!!.child("profile").child(user?.uid!!).child("address")
//                databaseReference.child(clickedAddress).updateChildren(map0)
//                Log.d("HHHHHHHHHH","333333333333332")
//                databaseReference.child(currentPriorAddressId).updateChildren(map)
//                Log.d("HHHHHHHHHH","333333333333331")
//                databaseReference.child(clickedAddress).child("priority").setValue("1")
//                databaseReference.child(currentPriorAddressId).child("priority").setValue("0")
//                addressList.clear()
//                val intent = Intent(context, AddressRefreshActivity::class.java)
//                context.startActivity(intent)
//                (context as Activity).finish()
//            }
//        })

    }

    override fun getItemCount(): Int {

        return addressList.size
    }

    inner class AddressViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val id : TextView = itemView.findViewById(R.id.tvId)
        val name : TextView = itemView.findViewById(R.id.tvName)
        val lineOne : TextView = itemView.findViewById(R.id.tvLineOne)
        val lineTwo : TextView = itemView.findViewById(R.id.tvLineTwo)
        val city : TextView = itemView.findViewById(R.id.tvCity)
        val state : TextView = itemView.findViewById(R.id.tvState)
        val pinCode : TextView = itemView.findViewById(R.id.tvPincode)
        val phoneNo : TextView = itemView.findViewById(R.id.phoneNo)

        val deleteAddress : TextView
        val selectedAddess : TextView

        init {
            deleteAddress = itemView.findViewById(R.id.tvDelete)
            deleteAddress.setOnClickListener { deleteSelectedAddress(it) }

            selectedAddess = itemView.findViewById(R.id.btnPriorAddress)
            selectedAddess.setOnClickListener { chh(it) }
        }

        private fun deleteSelectedAddress(view : View) {
            val clickedAddress = id.text.toString()
            Log.d("Clicked on address : ", clickedAddress)

            val user = FirebaseAuth.getInstance().currentUser
            val databaseReference = FirebaseDatabase.getInstance()?.reference!!.child("profile").child(user?.uid!!).child("address").child(clickedAddress)
            databaseReference.removeValue()
            addressList.clear()
            notifyDataSetChanged()
        }

        private fun chh(view : View){
            val clickedAddressId = id.text.toString()
            val user = FirebaseAuth.getInstance().currentUser
            val databaseReference = FirebaseDatabase.getInstance()?.reference!!.child("profile").child(user?.uid!!).child("address")

            val newPriorAddress = mapOf<String, String>("city" to city.text.toString(),
                                                        "id" to id.text.toString(),
                                                        "lineOne" to lineOne.text.toString(),
                                                        "lineTwo" to lineTwo.text.toString(),
                                                        "name" to name.text.toString(),
                                                        "phoneNo" to phoneNo.text.toString(),
                                                        "pinCode" to pinCode.text.toString(),
                                                        "priority" to "1",
                                                        "state" to state.text.toString())
            val oldPriorAddress = mapOf<String, String>("city" to oldPriorAddressRefrence.city.toString(),
                                                        "id" to oldPriorAddressRefrence.id.toString(),
                                                        "lineOne" to oldPriorAddressRefrence.lineOne.toString(),
                                                        "lineTwo" to oldPriorAddressRefrence.lineTwo.toString(),
                                                        "name" to oldPriorAddressRefrence.name.toString(),
                                                        "phoneNo" to oldPriorAddressRefrence.phoneNo.toString(),
                                                        "pinCode" to oldPriorAddressRefrence.pinCode.toString(),
                                                        "priority" to "0",
                                                        "state" to oldPriorAddressRefrence.state.toString())
            val updatedAddressPriority = mapOf<String, Any>(clickedAddressId to newPriorAddress, oldPriorAddressRefrence.id.toString() to oldPriorAddress)

            databaseReference.updateChildren(updatedAddressPriority).addOnSuccessListener {
                Log.d("876878768989","DONE!!!!!!!")
            }.addOnFailureListener {
                Log.d("876878768989","NOT DONE!!!!!!!")
            }
            addressList.clear()
//            databaseReference.child(clickedAddressId).updateChildren(newPriorAddress).addOnSuccessListener {
//                //addressList.clear()
//                Log.d("876878768989","DONE!!!!!!!")
//            }.addOnFailureListener {
//                Log.d("0876757687", "NOT DONE !!!!!!")
//            }.addOnCompleteListener{
//                addressList.clear()
//            }

//            databaseReference.child(currentPriorAddressId).updateChildren(oldPriorAddress).addOnSuccessListener {
//               // addressList.clear()
//                Log.d("876878768989","DONE!!!!!!!")
//            }.addOnFailureListener {
//                Log.d("0876757687", "NOT DONE !!!!!!")
//            }
            Log.d("HHHHHHHHHH","333333333333331")
        }


    }

}