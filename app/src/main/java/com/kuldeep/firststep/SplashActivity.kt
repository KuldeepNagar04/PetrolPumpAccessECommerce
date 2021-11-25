package com.kuldeep.firststep

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashActivity: AppCompatActivity() {

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 2600 // 1 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash_screen)

        YoYo.with(Techniques.RollIn).duration(2000).repeat(0).playOn(imageView)
        YoYo.with(Techniques.RollIn).duration(2000).repeat(0).playOn(designedBy)
        YoYo.with(Techniques.RollIn).duration(2000).repeat(0).playOn(tvFeo)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity
            startActivity(Intent(this,LoginActivity::class.java))
            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }

}