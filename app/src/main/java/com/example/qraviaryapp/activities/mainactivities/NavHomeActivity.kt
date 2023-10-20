package com.example.qraviaryapp.activities.mainactivities

import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.BirdFilterActivity
import com.example.qraviaryapp.adapter.MyFirebaseMessagingService
import com.example.qraviaryapp.databinding.ActivityNavHomeBinding
import com.example.qraviaryapp.fragments.NavFragments.*
import com.example.qraviaryapp.fragments.SettingsFragment
import com.example.qraviaryapp.monitoring.MonitoringFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class NavHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding : ActivityNavHomeBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var gso: GoogleSignInOptions
    private lateinit  var gsc: GoogleSignInClient
    private lateinit var menu: Menu


    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        binding = ActivityNavHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val MyFirebaseMessagingService = MyFirebaseMessagingService()
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open_nav, R.string.close_nav)
            .apply {
                drawerLayout.addDrawerListener(this)
                syncState()
            }
        toggle.drawerArrowDrawable.color = ContextCompat.getColor(this, R.color.black_white)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, MonitoringFragment()).commit()
            navigationView.menu.getItem(0).isChecked = true

        }

        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "My token: $token")
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
        

        checkElapsedTime()
    }
    private fun checkElapsedTime() {
        val appStoppedTime = sharedPreferences.getLong("appStoppedTime", 0)
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTime = currentTimeMillis - appStoppedTime
        val thresholdTime = 60000 // 5 seconds in milliseconds

        if (elapsedTime >= thresholdTime) {
            // Show the splash activity
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onPause() {
        Log.d(ContentValues.TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        val currentTimeMillis = System.currentTimeMillis()
        sharedPreferences.edit().putLong("appStoppedTime", currentTimeMillis).apply()
    }

    override fun onDestroy() {
        Log.d(ContentValues.TAG, "onDestroy")
        super.onDestroy()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment: Fragment
        val title: String
        val isMonitoringFragment: Boolean

        when (item.itemId) {
            R.id.nav_birds -> {
                fragment = BirdsFragment()
                title = "Birds"
                isMonitoringFragment = false

            }
            R.id.nav_pairs -> {
                fragment = PairsFragment()
                title = "Pairs"
                isMonitoringFragment = false
            }
            R.id.nav_cages -> {
                fragment = CagesFragment()
                title = "Cages"
                isMonitoringFragment = false
            }
            R.id.nav_statistics -> {
                fragment = StatisticsFragment()
                title = "Statistics"

                isMonitoringFragment = false
            }
            R.id.nav_mutations -> {
                fragment = MutationsFragment()
                title = "Mutations"
                isMonitoringFragment = false
            }
            R.id.nav_aldulting -> {
                fragment = AdultingFragment()
                title = "Adulting"
                isMonitoringFragment = false
            }
            R.id.nav_expenses -> {
                fragment = ExpensesFragment()
                title = "Expenses"

                isMonitoringFragment = false
            }
            R.id.nav_sales -> {
                fragment = NavSalesFragment()
                title = "Sales"
                isMonitoringFragment = false
            }
            R.id.nav_purchases -> {
                fragment = NavPurchasesFragment()
                title = "Purchases"
                isMonitoringFragment = false
            }
            R.id.nav_balance -> {
                fragment = BalanceFragment()
                title = "Balance"

                isMonitoringFragment = false
            }
            R.id.nav_gallery -> {
                fragment = GalleryFragment()
                title = "Gallery"
                isMonitoringFragment = false
            }
            R.id.nav_incubating -> {
                fragment = IncubatingFragment()
                title = "Incubating"

                isMonitoringFragment = false
            }
            R.id.nav_settings -> {
                fragment = SettingsFragment()
                title = "Settings"
                isMonitoringFragment = false
            }
            R.id.nav_categories -> {
                fragment = CategoriesFragment()
                title = "Categories"
                isMonitoringFragment = false
            }
            R.id.nav_logout -> {
                showLogoutConfirmationDialog()
                return true // Early return, no need to continue
            }
            else -> {
                fragment = MonitoringFragment() // Default fragment
                title = "Monitoring" // Default title
                hideMenuItems()
                isMonitoringFragment = true
            }
        }
        if (!isMonitoringFragment) {
            if(title == "Expenses" || title == "Purchases" || title == "Sales"){
                toolbar.elevation = 0f
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.toolbarcolor)))
            }else{
            toolbar.elevation = resources.getDimension(R.dimen.toolbar_elevation)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.toolbarcolor)))
                }
        } else {
            if (title.equals("Balance")){
                toolbar.elevation = 0f
                supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.white_black)))
            }
            else {
                toolbar.elevation = 0f
                supportActionBar?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            this,
                            R.color.toolbarcolor
                        )
                    )
                )
            }
        }
        if (title == "Monitoring" || title == "Cages" || title == "Statistics" || title == "Mutations"||
                title == "Gallery" || title == "Incubating" || title == "Adulting" || title == "Balance" ||
                title == "Categories"||title == "Settings") {
            hideMenuItems()
        } else {
            showMenuItems()
        }

        // Set the toolbar title
        replaceFragment(fragment)
        drawerLayout.closeDrawer(GravityCompat.START)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = title


        return true
    }
    private fun hideMenuItems() {
        menu.findItem(R.id.ic_filter).isVisible = false
        // Add more menu items to hide if needed
    }

    private fun showMenuItems() {
        menu.findItem(R.id.ic_filter).isVisible = true

        // Add more menu items to show if needed
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        this.menu = menu
        hideMenuItems()
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_filter -> {
                val intent = Intent(this, BirdFilterActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { _, _ ->
            signOut()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    fun signOut() {
        mAuth.signOut()
        gsc.signOut().addOnCompleteListener {
            startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout,fragment)
            .commit()
    }

}