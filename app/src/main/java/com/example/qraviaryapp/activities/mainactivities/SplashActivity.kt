package com.example.qraviaryapp.activities.mainactivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate

import com.example.qraviaryapp.R
import com.google.firebase.database.FirebaseDatabase


class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferencess: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferencess = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        if (sharedPreferencess.getBoolean("APP_OPENED_BEFORE", false)) {
            // Skip SplashActivity and go to NavHomeActivity
            goToNavHome()
        } else {
            onSaveDarkModeReference()
            Handler().postDelayed({
                // Mark that the app has been opened at least once
                sharedPreferencess.edit().putBoolean("APP_OPENED_BEFORE", true).apply()

                val intent =
                    Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }
    private fun goToNavHome() {
        val intent = Intent(this@SplashActivity, NavHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun onSaveDarkModeReference(){
        val darkModePref = sharedPreferencess.getInt("DARK_MODE", 0);

        when(darkModePref){
            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

            }
            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            3 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

    }
}
