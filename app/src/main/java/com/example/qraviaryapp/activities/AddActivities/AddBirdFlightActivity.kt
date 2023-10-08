package com.example.qraviaryapp.activities.AddActivities

import BirdData
import BirdDataListener
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.BirdListActivity
import com.example.qraviaryapp.activities.mainactivities.NavHomeActivity
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.AddFragment.AddGalleryFragment
import com.example.qraviaryapp.fragments.AddFragment.BasicFlightFragment
import com.example.qraviaryapp.fragments.AddFragment.BasicFragment
import com.example.qraviaryapp.fragments.AddFragment.OriginFragment
import com.example.qraviaryapp.fragments.NavFragments.BirdsFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddBirdFlightActivity : AppCompatActivity(), BirdDataListener {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var spinner: Spinner
    private val BasicFragment: BasicFragment = BasicFragment()
    val fragmentAdapter = FragmentAdapter(supportFragmentManager)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_add_bird)

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
            "<font color='$abcolortitle'>Add Flight Bird</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)


        viewPager = findViewById(R.id.viewPager)
        tablayout = findViewById(R.id.tablayout)
        viewPager.offscreenPageLimit = 3


        val basicFragmentDeferred = BasicFlightFragment()
        val originFragmentDeferred = OriginFragment()
        val galleryFragmentDeferred = AddGalleryFragment()

        val basicFragment = basicFragmentDeferred
        val originFragment = originFragmentDeferred
        val galleryFragment = galleryFragmentDeferred

        fragmentAdapter.addFragment(basicFragment, "Basic")
        fragmentAdapter.addFragment(originFragment, "Origin")
        fragmentAdapter.addFragment(galleryFragment, "Gallery")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_bird, menu)

        val saveMenuItem = menu.findItem(R.id.action_save)

        // Check if night mode is enabled
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            // Set the text color to white for night mode
            saveMenuItem.title = HtmlCompat.fromHtml(
                "<font color='#FFFFFF'>Save</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            // Set the text color to black for non-night mode
            saveMenuItem.title = HtmlCompat.fromHtml(
                "<font color='#000000'>Save</font>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }



        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {

                val basicFragment = fragmentAdapter.getItem(0) as BasicFlightFragment
                val originFragment = fragmentAdapter.getItem(1) as OriginFragment
                val galleryFragment = fragmentAdapter.getItem(2) as AddGalleryFragment

                lifecycleScope.launch {
                    try {
                        var birdId = ""
                        var newBundle: Bundle = Bundle()
                        var flightId = ""

                        basicFragment.birdDataGetters { receivedBirdId, receivedFlightId, receivedNewBundle ->
                            birdId = receivedBirdId
                            flightId = receivedFlightId
                            newBundle = receivedNewBundle

                            originFragment.addFlightOrigin(birdId, flightId, newBundle)
                            { callBackMotherKey, callBackFatherKey, descendantfatherkey, descendantmotherkey ->
                                galleryFragment.FlightuploadImageToStorage(birdId, flightId, newBundle,
                                    callBackMotherKey, callBackFatherKey, descendantfatherkey, descendantmotherkey)
                            }


                            onBackPressed()
                            finish()

                        }




                    } catch (e: NullPointerException) {
                        Toast.makeText(
                            applicationContext,
                            "Gender and Provenance in Origin tab must not be empty...",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }


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
    /* override fun onBackPressed() {
         // Create an intent to navigate back to BirdListActivity
         val intent = Intent(this, BirdListActivity::class.java)
         startActivity(intent)
         finish() // Finish the current activity (AddBirdActivity)
     }*/

    fun openDatePicker(view: View) {}
    fun openDatebandPicker(view: View) {}
    fun openDatebirthPicker(view: View) {}
    fun openSoldDatePicker(view: View) {}
    fun openDeathDatePicker(view: View) {}
    fun openExchangeDatePicker(view: View) {}
    fun openLostDatePicker(view: View) {}
    fun openDonatedDatePicker(view: View) {}
    fun openBoughtDatePicker(view: View) {}

    override fun onBirdDataSaved(id: String?) {
    }

    override fun onBirdDateSaved(birdData: BirdData) {
    }

}
