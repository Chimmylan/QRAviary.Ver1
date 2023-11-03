package com.example.qraviaryapp.activities.AddActivities

import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButtonToggleGroup

class GenerateQrActivity : AppCompatActivity() {
        private lateinit var togglebutton: MaterialButtonToggleGroup
        private lateinit var btnegg: Button
        private lateinit var btnbird: Button
        private lateinit var egglayout: LinearLayout
        private lateinit var birdlayout: LinearLayout
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
            }
            setContentView(R.layout.activity_generate_qr)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
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
            "<font color='$abcolortitle'>Generate QR</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
            togglebutton =findViewById(R.id.toggleButtonGroup)
            egglayout = findViewById(R.id.egglayout)
            birdlayout = findViewById(R.id.birdlayout)
            btnegg = findViewById(R.id.btnegg)


            togglebutton.addOnButtonCheckedListener { group, checkedId, isChecked ->

                if (isChecked) {
                    when (checkedId) {
                        R.id.btnegg -> {
                            egglayout.visibility = View.VISIBLE
                            birdlayout.visibility = View.GONE

                        }
                        R.id.btnbird -> {
                            birdlayout.visibility = View.VISIBLE
                            egglayout.visibility = View.GONE
                        }
                    }
                }

            }
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