package com.kuldeep.firststep

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.kuldeep.firststep.Adapter.ProductImagesAdapter
import com.kuldeep.firststep.Adapter.ReviewAdapter
import com.kuldeep.firststep.Model.Category
import com.kuldeep.firststep.Model.Product
import com.kuldeep.firststep.Model.ProductImages
import com.kuldeep.firststep.Model.Review
import kotlinx.android.synthetic.main.activity_product_description.*
import java.util.*


class ProductDescriptionActivity : AppCompatActivity() {

    private var productId: String? = null

    private lateinit var reviewRecyclerview : RecyclerView
    private lateinit var reviewArrayList : ArrayList<Review>
    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_description)



        reviewRecyclerview = findViewById(R.id.reviewList)
        reviewRecyclerview.layoutManager = LinearLayoutManager(this)
        reviewRecyclerview.setHasFixedSize(true)
        reviewArrayList = arrayListOf()

        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("product")

        productId = intent.extras?.getString("productToBeDisplayed")
        Log.d("bsofoirbgsri",productId)
        val productreference = databaseReference?.child(productId!!)


        addToCartButton.setOnClickListener {
            intent = Intent(this,CartActivity::class.java)
            intent.putExtra("productToBeAdded",productId)
            startActivity(intent)
        }


        Log.d("sjdoh 9wue98njdsoi","kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" )

        val docRef = FirebaseFirestore.getInstance().collection("product").document(productId!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("lklwue98njdsoi","kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" )
                    Log.d("llllwue98njdsoi",document.get("price").toString() )
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    product_brand.text = document.get("brand").toString()
                    product_category.text =  document.get("category").toString()
                    product_name.text = document.get("productName").toString()
                    thePriceOfProduct.text = "Price: ₹"+document.get("price").toString()
                    listviewinformation.text = document.get("description").toString()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                Log.d("34wue98njdsoi","kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" )
            }


        Log.d("wue98njdsoi","kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk" )

//        productreference.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.d("sjdoh 9wue98njdsoi",snapshot.toString() )
//                val productDetails : ArrayList<String> = ArrayList()
//                Log.d("bjdisrhu",snapshot.child("category").toString())
//
//                product_brand.text = snapshot.child("brand").value.toString()
//                product_category.text =  snapshot.child("category").value.toString()
//                product_name.text = snapshot.child("productName").value.toString()
//                thePriceOfProduct.text = "Price: ₹"+snapshot.child("price").value.toString()
////                listviewinformation.text = productDetails.get(3)
////                Glide.with(this@ProductDescriptionActivity).load(productDetails.get(4)).into(photo)
//
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })


//        val productImagesArrayList = arrayListOf<ProductImages>()
//        val productImagesRecyclerview : RecyclerView
//        productImagesRecyclerview = findViewById(R.id.productImagesList)
//        productImagesRecyclerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
//        productImagesRecyclerview.setHasFixedSize(true)
        val dbReview = FirebaseFirestore.getInstance()
        dbReview.collection("product")
            .document(productId!!)
            .collection("reviews")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val review : Review = document.toObject(Review::class.java)
                    reviewArrayList.add(review)
                }
                reviewRecyclerview.adapter = ReviewAdapter( reviewArrayList)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

//        val reviewReference = databaseReference?.child(productId!!).child("reviews")
//        reviewReference.addValueEventListener(object : ValueEventListener{
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    for (reviewSnapshot in snapshot.children){
//                        Log.d("HHHHHHHHHH","44444444")
//                        Log.d("jbdfhoie f",reviewSnapshot.toString())
//
//                        val review = reviewSnapshot.getValue(Review::class.java)
//                        Log.d("HHHHHHHHHH","5555555555555")
//                        reviewArrayList.add(review!!)
//
//                    }
//                    Log.d("HHHHHHHHHH","666666666")
//                    reviewRecyclerview.adapter = ReviewAdapter(reviewArrayList)
//                    Log.d("HHHHHHHHHH","777777777777")
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })

        val productImagesArrayList = arrayListOf<ProductImages>()
        val productImagesRecyclerview : RecyclerView
        productImagesRecyclerview = findViewById(R.id.productImagesList)
        productImagesRecyclerview.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        productImagesRecyclerview.setHasFixedSize(true)
        val db = FirebaseFirestore.getInstance()
        db.collection("product")
            .document(productId!!)
            .collection("image_resource")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val productImage : ProductImages = document.toObject(ProductImages::class.java)
                    productImagesArrayList.add(productImage)
                }
                productImagesRecyclerview.adapter = ProductImagesAdapter(this, productImagesArrayList)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
}