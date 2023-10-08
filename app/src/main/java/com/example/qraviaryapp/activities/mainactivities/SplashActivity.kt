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
        onSaveDarkModeReference()
        val appStoppedTime = sharedPreferencess.getLong("appStoppedTime", 0)
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTime = currentTimeMillis - appStoppedTime
        val thresholdTime = 60000



        if (elapsedTime >= thresholdTime){
            Handler().postDelayed({
                val currentTimeMillis = System.currentTimeMillis()
                sharedPreferencess.edit().putLong("appStoppedTime", currentTimeMillis).apply()
                val intent =
                    Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }else{
            val intent =
                Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

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