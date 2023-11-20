package com.example.qraviaryapp.activities.AddActivities

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.AddFragment.BasicFragment
import com.example.qraviaryapp.fragments.AddFragment.OriginFragment
import com.google.android.material.tabs.TabLayout

class AddCageActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_cage)

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
        supportActionBar?.title = HtmlCompat.fromHtml("<font color='$abcolortitle'>Add Cage</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        viewPager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tablayout)


        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(BasicFragment(),"Basic")
        fragmentAdapter.addFragment(OriginFragment(),"Origin")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)

        val saveMenuItem = menu.findItem(R.id.action_save)

        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the text color to white for night mode
            saveMenuItem.title = HtmlCompat.fromHtml("<font color='#FFFFFF'>Save</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            // Set the text color to black for non-night mode
            saveMenuItem.title = HtmlCompat.fromHtml("<font color='#000000'>Save</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                // Handle the Save button click here
                // Implement the logic to save the bird or perform any action you need.
                true
            }
            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
