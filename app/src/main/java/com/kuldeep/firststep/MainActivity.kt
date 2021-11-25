package com.kuldeep.firststep

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var togge :ActionBarDrawerToggle

    lateinit var mGoogleSignInClient: GoogleSignInClient

    //creating firebase ref
    lateinit var auth: FirebaseAuth
    var databaseReference :  DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initializing firebase ref
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadUser()



        //Side Menu
        val drawerLayout:DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        togge = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)

        drawerLayout.addDrawerListener(togge)
        togge.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
              //  R.id.myAccount-> { startActivity(Intent(this,MyAccountActivity::class.java))}
              //  R.id.jinkiMyAccount-> { startActivity(Intent(this,MainActivity::class.java))}
                R.id.nav_home-> { startActivity(Intent(this,HomeActivity::class.java))}
                R.id.nav_myOrders-> Toast.makeText(this,"Clicked home",Toast.LENGTH_SHORT).show()
                R.id.nav_myWishlist-> Toast.makeText(this,"Clicked home",Toast.LENGTH_SHORT).show()
            }
            true
        }

        signOutuser.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
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
       // val userreference = databaseReference?.child(user?.uid!!)
       Log.d("uhagfyugaivgbsr",user.toString())
        signOutuser.text = "SignOutUser "+user?.email.toString()
    }

}