package com.example.qraviaryapp.activities.mainactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.qraviaryapp.R

class NameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name)
    }

    fun change(view: View) {
        startActivity(Intent(this@NameActivity, HomeActivity::class.java))
    }
    fun skip(view: View) {
        startActivity(Intent(this@NameActivity, HomeActivity::class.java))
    }
}