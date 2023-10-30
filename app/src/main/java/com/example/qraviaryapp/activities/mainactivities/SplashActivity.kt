package com.example.qraviaryapp.activities.mainactivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate

import com.example.qraviaryapp.R
import com.facebook.login.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferencess: SharedPreferences
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharedPreferencess = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        onSaveDarkModeReference()
        val appStoppedTime = sharedPreferencess.getLong("appStoppedTime", 0)
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTime = currentTimeMillis - appStoppedTime
        val thresholdTime = 60000

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null){
            val intent =
                Intent(this@SplashActivity, NavHomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }






        if (elapsedTime >= thresholdTime){
            Handler().postDelayed({
                val currentTimeMillis = System.currentTimeMillis()
                sharedPreferencess.edit().putLong("appStoppedTime", currentTimeMillis).apply()

                if (isAnyAccountOccupyingASlot()){
                    val intent =
                        Intent(this@SplashActivity, SaveLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent =
                        Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }


            }, 3000)
        }else{
            if (isAnyAccountOccupyingASlot()){
                val intent =
                    Intent(this@SplashActivity, SaveLoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent =
                    Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


    }

    fun isAnyAccountOccupyingASlot(): Boolean {
        val maxAccounts = 4
        for (i in 1..maxAccounts) {
            val userKey = "user$i"
            if (sharedPreferencess.contains(userKey)) {
                return true
            }
        }
        return false
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