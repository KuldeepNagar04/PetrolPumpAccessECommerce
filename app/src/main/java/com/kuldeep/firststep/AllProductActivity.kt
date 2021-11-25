package com.kuldeep.firststep

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.kuldeep.firststep.Adapter.AllProductAdapter
import com.kuldeep.firststep.Adapter.ReviewAdapter
import com.kuldeep.firststep.Model.Product
import com.kuldeep.firststep.Model.Review
import kotlinx.android.synthetic.main.activity_allproduct.*

class AllProductActivity: AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var allProductRecyclerview : RecyclerView
    private lateinit var allProductArrayList : ArrayList<Product>
    private lateinit var allProducts : ArrayList<Product>
    private lateinit var filterKeys : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allproduct)

        allProductArrayList = arrayListOf()
        allProducts =  arrayListOf()

        allProductRecyclerview = findViewById(R.id.allProductList)
        allProductRecyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        allProductRecyclerview.setHasFixedSize(true)
        allProductRecyclerview.adapter = AllProductAdapter(allProductArrayList,this@AllProductActivity)

        displayAllProductData()

        // Sorting products
        val sortKeys = arrayOf("Sort By","Price: Low to High", "Price: High to Low", "Newest First")
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,sortKeys)
        btnSort.adapter =  sortAdapter
        btnSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(sortKeys[p2].equals("Price: Low to High")) {
                    Log.d("ferwtgrgwstgt",allProductArrayList.toString())
                    allProductArrayList.sortBy { selectorPrice(it) }
                    allProducts.sortBy { selectorPrice(it) }
                    Log.d("ferwtgrgwstgt",allProductArrayList.toString())
                    allProductRecyclerview.adapter?.notifyDataSetChanged()
                }else if (sortKeys[p2].equals("Price: High to Low")) {
                    allProductArrayList.sortByDescending { selectorPrice(it) }
                    allProducts.sortByDescending { selectorPrice(it) }
                    allProductRecyclerview.adapter?.notifyDataSetChanged()
                }else if(sortKeys[p2].equals("Newest First")) {
                    allProductArrayList.sortByDescending { selectorDate(it) }
                    allProducts.sortByDescending { selectorDate(it) }
                    allProductRecyclerview.adapter?.notifyDataSetChanged()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //Filtering Keys
        filterKeys = arrayListOf("Brand","Category")


        // filtering brand
        val brandKeys = arrayOf("Brand", "Bharat Petroleum", "Indian Oil", "HP", "Reliance")
        val brandAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,brandKeys)
        btnBrand.adapter =  brandAdapter
        btnBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filterKeys.set(0,brandKeys[p2])
                filterProducts()
                Log.d("3kjshfpioshre",filterKeys.toString())
//                if(!brandKeys[p2].equals("Brand")) {
//                    allProductArrayList.clear()
//                    for (prodcut : Product in allProducts) {
//                        if(prodcut.brand.equals(brandKeys[p2])) {
//                            allProductArrayList.add(prodcut)
//                        }
//                    }
//                }
//                allProductRecyclerview.adapter?.notifyDataSetChanged()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        // filtering brand
        val categoryKeys = arrayOf("Category", "Fire Extinguisher", "Oils", "Clothing", "Nozzel", "Measuring Jars")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,categoryKeys)
        btnCategory.adapter =  categoryAdapter
        btnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filterKeys.set(1,categoryKeys[p2])
                filterProducts()
                Log.d("2kjshfpioshre",filterKeys.toString())
//                if(!brandKeys[p2].equals("Brand")) {
//                    allProductArrayList.clear()
//                    for (prodcut : Product in allProducts) {
//                        if(prodcut.brand.equals(brandKeys[p2])) {
//                            allProductArrayList.add(prodcut)
//                        }
//                    }
//                }
//                allProductRecyclerview.adapter?.notifyDataSetChanged()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }






    }


    private fun displayAllProductData(){
//        allProductArrayList.clear()

        val db = FirebaseFirestore.getInstance()
        db.collection("product")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val product : Product = document.toObject(Product::class.java)
                    allProductArrayList.add(product)
                    allProducts.add(product)
                }
                allProductRecyclerview.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }


//        databaseReference = FirebaseDatabase.getInstance().reference.child("product")
//
//        databaseReference.addValueEventListener(object : ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                if (snapshot.exists()){
//
//                    for (productSnapshot in snapshot.children){
//                        Log.d("HHHHHHHHHH","44444444")
//                        Log.d("jbdfhoie f",productSnapshot.toString())
//
//                        val product = productSnapshot.getValue(Product::class.java)
//                        Log.d("HHHHHHHHHH","5555555555555")
//                        allProductArrayList.add(product!!)
//                        allProducts.add(product!!)
//
//                    }
//
//                    Log.d("HHHHHHHHHH","666666666")
//                    allProductRecyclerview.adapter?.notifyDataSetChanged()
//                    Log.d("HHHHHHHHHH","777777777777")
//
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//
//        })

    }

    private fun filterProducts() {
        if(!(filterKeys[0].equals("Brand") && filterKeys[1].equals("Category"))) {
//            allProductArrayList = allProducts
//            allProductRecyclerview.adapter?.notifyDataSetChanged()
//
//        } else {
            if (filterKeys[0].equals("Brand")) {
                allProductArrayList.clear()
                for (prodcut : Product in allProducts) {
                    if(prodcut.category.equals(filterKeys[1])) {
                        allProductArrayList.add(prodcut)
                    }
                }
            } else if (filterKeys[1].equals("Category")) {
                allProductArrayList.clear()
                for (prodcut : Product in allProducts) {
                    if(prodcut.brand.equals(filterKeys[0])) {
                        allProductArrayList.add(prodcut)
                    }
                }
            } else {
                allProductArrayList.clear()
                for (prodcut : Product in allProducts) {
                    if(prodcut.brand.equals(filterKeys[0]) && prodcut.category.equals(filterKeys[1])) {
                        allProductArrayList.add(prodcut)
                    }
                }
            }
        } else {
            allProductArrayList.clear()
            for (prod : Product in allProducts){
                allProductArrayList.add(prod)
            }
        }
        allProductRecyclerview.adapter?.notifyDataSetChanged()
    }

    fun selectorPrice(p: Product): String? = p.price
    fun selectorDate(p: Product): String? = p.dateAdded

}