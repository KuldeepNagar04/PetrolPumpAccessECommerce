package com.kuldeep.firststep

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.kuldeep.firststep.Adapter.CategoryAdapter
import com.kuldeep.firststep.Model.Category
import com.kuldeep.firststep.Model.Product
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.*


class HomeActivity : AppCompatActivity() {

    lateinit var togge :ActionBarDrawerToggle

    lateinit var auth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var databaseReference: DatabaseReference
    var database: FirebaseDatabase? = null
    private lateinit var productRecyclerview : RecyclerView
    private lateinit var productArrayList : ArrayList<Product>

    private lateinit var categoryRecyclerview : RecyclerView
    private lateinit var categoryArrayList : ArrayList<Category>

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout= findViewById(R.id.homeLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        togge = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)

        drawerLayout.addDrawerListener(togge)
        togge.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.myAccount-> { startActivity(Intent(this,MyAccountActivity::class.java))}
                R.id.jinkiMyAccount-> { startActivity(Intent(this,MainActivity::class.java))}
                R.id.nav_home-> { startActivity(Intent(this,HomeActivity::class.java))}
                R.id.menu_cart-> {startActivity(Intent(this,CartActivity::class.java))}
                R.id.nav_myOrders-> Toast.makeText(this,"Clicked home", Toast.LENGTH_SHORT).show()
                R.id.nav_myWishlist-> Toast.makeText(this,"Clicked home", Toast.LENGTH_SHORT).show()
                R.id.menu_cart-> {startActivity(Intent(this,CartActivity::class.java))}
            }
            true
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadUser()

        categoryRecyclerview = findViewById(R.id.category_list)
        categoryRecyclerview.layoutManager = LinearLayoutManager(this)
        categoryRecyclerview.setHasFixedSize(true)

        categoryArrayList =  arrayListOf<Category>()

//        productRecyclerview = findViewById(R.id.product_list)
//        productRecyclerview.layoutManager = LinearLayoutManager(this)
//        productRecyclerview.setHasFixedSize(true)

        productArrayList = arrayListOf<Product>()
        categoryRecyclerview.adapter = CategoryAdapter(categoryArrayList,productArrayList,this)
//        productRecyclerview.adapter = ProductAdapter(productArrayList,this)

//        displayCategory()
        displayFromFireStore()

        auth= FirebaseAuth.getInstance()
        var currentUser=auth.currentUser



//        findViewById<Spinner>(R.id.btnSort).setOnClickListener {
//            productArrayList.sortBy { selector(it) }
//            var key = btnSort.selectedItem.toString()
//            Log.d("jfhwsuerhgoijnivgogw", key)
//            displayProducts("price")
//        }

//        val sortKeys = arrayOf("Sort By", "Price: Low to High", "Price: High to Low", "Newest First")
//        val sortAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,sortKeys)

//        btnSort.adapter =  sortAdapter

//        btnSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                var sortKey = sortKeys[p2]
//                //displayProducts(sortKey)
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//        }





        btn_all_product.setOnClickListener {
            startActivity(Intent(this,AllProductActivity::class.java))
        }

        btn_clothing.setOnClickListener {
            intent = Intent(this,CategoryProducts::class.java)
            intent.putExtra("categoryChoosed","Clothing")
            startActivity(intent)
        }

        btn_fire_ext.setOnClickListener {
            intent = Intent(this,CategoryProducts::class.java)
            intent.putExtra("categoryChoosed","Fire Extinguisher")
            startActivity(intent)
        }

        btn_nozzles.setOnClickListener {
            intent = Intent(this,CategoryProducts::class.java)
            intent.putExtra("categoryChoosed","Nozzel")
            startActivity(intent)
        }

        btn_measuring_jar.setOnClickListener {
            intent = Intent(this,CategoryProducts::class.java)
            intent.putExtra("categoryChoosed","Clothing")
            startActivity(intent)
        }

        var db = FirebaseFirestore.getInstance()
            .collection("profiles")
            .document(currentUser?.uid!!).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("gsrtgtrfg","8888888888")
                    Log.d("gsrtgtrfg","8888888888")
                    Log.d("gsrtgtrfg","8888888888")
                    Log.d("gsrtgtrfg","8888888888")
                    Log.d("gsrtgtrfg","8888888888")
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(togge.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.icon_menu,menu);
        return true
    }


    private fun loadUser() {
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        userreference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userPhone.text = "+91"+snapshot.child("phoneNumber").value.toString()
                userName.text ="Hello, "+snapshot.child("firstname").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun displayCategory() {
        productArrayList.clear()
        databaseReference = FirebaseDatabase.getInstance().reference.child("product")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (productSnapshot in snapshot.children){
                        //Log.d("HHHHHHHHHH","Line11111111")
                        //Log.d("HHHHHHHH",cartSnapshot.toString())

                        val product = productSnapshot.getValue(Product::class.java)
                        //Log.d("HHHHHHHHHH","Line2222222")
                        //Log.d("HHHHHHHHH",cart.toString())
//                        if (product != null) {
//                            amount+= product.price?.toDouble() ?: 0.0
//                        }
                        productArrayList.add(product!!)

                    }
                    //Log.d("HHHHHHHHHH","Line3333333333")

                    categoryArrayList.add(Category("Clothing",productArrayList))
                    categoryArrayList.add(Category("Nozzels",productArrayList))
                    categoryArrayList.add(Category("Compresser",productArrayList))
                    categoryArrayList.add(Category("Fire Extinguisher",productArrayList))

                    categoryRecyclerview.adapter?.notifyDataSetChanged()
                    //productRecyclerview.adapter?.notifyDataSetChanged()
                    //Log.d("HHHHHHHHHH","Line444444444")


                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        })
    }

    private fun displayFromFireStore() {
        val db = FirebaseFirestore.getInstance()
        Log.d("jspiewp9gfionjgioeijfiv", db.toString())
        Log.d("jspiewp9gfionjgioeijfiv", db.collection("product").document("1").get().toString())
        db.collection("product")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("jseirughrefuhsfgo",task.toString())
                    for (document in task.result!!) {
                        Log.d(TAG, document.id + " => " + document.data)
                        val product: Product = document.toObject(Product::class.java)

//                        val product = document?.toObject<Product>()
                        productArrayList.add(product!!)
                        Log.d("ooooooooooooooooooo",product.toString())
                    }

                    val clothinigProductArrayList = arrayListOf<Product>()
                    val nozzlesProductArrayList = arrayListOf<Product>()
                    val fireExtinguisherProductArrayList = arrayListOf<Product>()
                    val measuringJarsProductArrayList = arrayListOf<Product>()

                    for(prod in productArrayList) {
                        if (prod.category.toString()== "Clothing"){
                            Log.d("ooooooooooooooooooo","ooooooooooooooooooooooooooooooooooo")
                            clothinigProductArrayList.add(prod)
                        }
                        else if (prod.category== "Fire Extinguisher"){
                            fireExtinguisherProductArrayList.add(prod)
                        }
                        else if (prod.category== "Nozzle"){
                            nozzlesProductArrayList.add(prod)
                        }
                        else if (prod.category== "Measuring Jar"){
                            measuringJarsProductArrayList.add(prod)
                        }
                    }
                    Log.d("klklklklklkklklklklk",clothinigProductArrayList.toString())
                    Log.d("klklklklklkklklklklk",fireExtinguisherProductArrayList.toString())
                    Log.d("klklklklklkklklklklk",nozzlesProductArrayList.toString())

                    categoryArrayList.add(Category("Clothing",clothinigProductArrayList))
                    categoryArrayList.add(Category("Fire Extinguisher",fireExtinguisherProductArrayList))
                    categoryArrayList.add(Category("Nozzles",nozzlesProductArrayList))
                    categoryArrayList.add(Category("Measuring Jars",measuringJarsProductArrayList))

                    categoryRecyclerview.adapter?.notifyDataSetChanged()


                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }

            }
    }



}

