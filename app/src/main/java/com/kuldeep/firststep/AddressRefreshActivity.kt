package com.kuldeep.firststep

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AddressRefreshActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_refresh_activity)

        intent = Intent(this, AddressBookActivity::class.java)
        startActivity(intent)

    }
}