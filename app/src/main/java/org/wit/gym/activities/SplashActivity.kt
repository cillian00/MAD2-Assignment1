package org.wit.gym.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import org.wit.placemark.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, GymListActivity::class.java))
            finish()
        }, 3000) // 3000 is the delay in milliseconds.
    }
}