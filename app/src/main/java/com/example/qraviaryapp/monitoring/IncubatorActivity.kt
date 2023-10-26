package com.example.qraviaryapp.monitoring

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.google.android.material.switchmaterial.SwitchMaterial

class IncubatorActivity : AppCompatActivity() {
    private lateinit var incubator: SwitchMaterial
    private lateinit var layouton: LinearLayout
    private lateinit var tvNum: TextView
    private var currentTemperature: Int = 32
    private var min: Int = 32
    private var max: Int = 35

    private lateinit var add: ImageView
    private lateinit var minus:ImageView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_incubator)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 4f
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.toolbarcolor
                )
            )
        )
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Incubator</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        add = findViewById(R.id.add)
        minus= findViewById(R.id.minus)
        incubator = findViewById(R.id.incubator)
        layouton = findViewById(R.id.layouton)
        tvNum = findViewById(R.id.text_view_progress)
//        numberPicker.minValue = 32
//        numberPicker.maxValue = 35


        tvNum.text = currentTemperature.toString() + "°C"
        incubator.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {

                layouton.visibility = View.VISIBLE
            } else {

                layouton.visibility = View.GONE
            }

        }

        add.setOnClickListener {
            incrementTemperature()
        }

        minus.setOnClickListener {
            decrementTemperature()
        }
    }

    private fun incrementTemperature() {
        if (currentTemperature < max) {
            currentTemperature++
            updateTemperatureTextView()
        }
    }

    private fun decrementTemperature() {
        if (currentTemperature > min) {
            currentTemperature--
            updateTemperatureTextView()
        }
    }

    private fun updateTemperatureTextView() {
        tvNum.text = currentTemperature.toString() + "°C"
    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}