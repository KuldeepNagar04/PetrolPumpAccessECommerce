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
import com.kuldeep.firststep.Model.Product
import kotlinx.android.synthetic.main.activity_allproduct.*
import kotlinx.android.synthetic.main.activity_categoryproduct.*
import kotlinx.android.synthetic.main.activity_categoryproduct.btnBrand
import kotlinx.android.synthetic.main.activity_categoryproduct.btnCategory
import kotlinx.android.synthetic.main.activity_categoryproduct.btnSort

class CategoryProducts: AppCompatActivity()  {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var categoryProductRecyclerview : RecyclerView
    private lateinit var categoryProductArrayList : ArrayList<Product>
    private lateinit var allProducts : ArrayList<Product>
    private lateinit var filterBrands : ArrayList<String>
    private lateinit var categoryChoosed : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allproduct)

        categoryChoosed = intent.extras?.getString("categoryChoosed").toString()
        Log.d("jskihfgrhseioj",categoryChoosed)
        val categoryKeys = arrayOf("Category")
        categoryKeys.set(0,categoryChoosed)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,categoryKeys)
        btnCategory.adapter =  categoryAdapter
        //tvCategory?.text = categoryChoosed

        categoryProductArrayList = arrayListOf()
        allProducts =  arrayListOf()

        categoryProductRecyclerview = findViewById(R.id.allProductList)
        categoryProductRecyclerview.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        categoryProductRecyclerview.setHasFixedSize(true)
        categoryProductRecyclerview.adapter = AllProductAdapter(categoryProductArrayList,this@CategoryProducts)

        gettingAllProductData()

        // Sorting products
        val sortKeys = arrayOf("Sort By","Price: Low to High", "Price: High to Low", "Newest First")
        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,sortKeys)
        btnSort.adapter = sortAdapter
        btnSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(sortKeys[p2].equals("Price: Low to High")) {
                    Log.d("ferwtgrgwstgt",categoryProductArrayList.toString())
                    categoryProductArrayList.sortBy { selectorPrice(it) }
                    allProducts.sortBy { selectorPrice(it) }
                    Log.d("ferwtgrgwstgt",categoryProductArrayList.toString())
                    categoryProductRecyclerview.adapter?.notifyDataSetChanged()
                }else if (sortKeys[p2].equals("Price: High to Low")) {
                    categoryProductArrayList.sortByDescending { selectorPrice(it) }
                    allProducts.sortByDescending { selectorPrice(it) }
                    categoryProductRecyclerview.adapter?.notifyDataSetChanged()
                }else if(sortKeys[p2].equals("Newest First")) {
                    categoryProductArrayList.sortByDescending { selectorDate(it) }
                    allProducts.sortByDescending { selectorDate(it) }
                    categoryProductRecyclerview.adapter?.notifyDataSetChanged()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //Filtering Keys
        filterBrands = arrayListOf("Brand")




        // filtering brand
        val brandKeys = arrayOf("Brand", "Bharat Petroleum", "Indian Oil", "HP", "Reliance")
        val brandAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,brandKeys)
        btnBrand.adapter =  brandAdapter
        btnBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                filterBrands.set(0,brandKeys[p2])
                filterProducts()
                Log.d("3kjshfpioshre",filterBrands.toString())

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }



    }


    private fun gettingAllProductData(){
//        allProductArrayList.clear()

        val db = FirebaseFirestore.getInstance()
        db.collection("product")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    val product : Product = document.toObject(Product::class.java)
                    categoryProductArrayList.add(product)
                    allProducts.add(product)
                }
                categoryProductRecyclerview.adapter?.notifyDataSetChanged()
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
//                        categoryProductArrayList.add(product!!)
//                        allProducts.add(product!!)
//
//                    }
//
//                    Log.d("HHHHHHHHHH","666666666")
//                    categoryProductRecyclerview.adapter?.notifyDataSetChanged()
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
        if(!(filterBrands[0].equals("Brand"))) {

            categoryProductArrayList.clear()
                for (prodcut : Product in allProducts) {
                    if(prodcut.brand.equals(filterBrands[0]) && prodcut.category.equals(categoryChoosed)) {
                        categoryProductArrayList.add(prodcut)
                    }
                }

        } else {
            categoryProductArrayList.clear()
            for (prod : Product in allProducts){
                if(prod.category.equals(categoryChoosed)) {
                    categoryProductArrayList.add(prod)
                }
            }
        }
        categoryProductRecyclerview.adapter?.notifyDataSetChanged()
    }

    fun selectorPrice(p: Product): String? = p.price
    fun selectorDate(p: Product): String? = p.dateAdded


}