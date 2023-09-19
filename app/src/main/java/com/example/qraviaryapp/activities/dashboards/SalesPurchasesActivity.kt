package com.example.qraviaryapp.activities.dashboards

import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.DetailedFragment.PurchasesFragment
import com.example.qraviaryapp.fragments.DetailedFragment.SalesFragment
import com.google.android.material.tabs.TabLayout

class SalesPurchasesActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    val fragmentAdapter = FragmentAdapter(supportFragmentManager)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        setContentView(R.layout.activity_sales_purchases)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.new_appbar_color
                )
            )
        )
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>Sale and Purchases</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the white back button for night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        } else {
            // Set the black back button for non-night mode
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_black)
        }
        viewPager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tablelayout)



        fragmentAdapter.addFragment(SalesFragment(),"Sales")
        fragmentAdapter.addFragment(PurchasesFragment(),"Purchases")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

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