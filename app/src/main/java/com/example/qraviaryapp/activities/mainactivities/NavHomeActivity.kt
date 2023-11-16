package com.example.qraviaryapp.activities.mainactivities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R

import com.example.qraviaryapp.activities.detailedactivities.BirdFilterActivity
import com.example.qraviaryapp.activities.detailedactivities.ExpensesFilterActivity
import com.example.qraviaryapp.activities.detailedactivities.PairFilteringActivity

import com.example.qraviaryapp.activities.detailedactivities.PurchaseFilterActivity
import com.example.qraviaryapp.activities.detailedactivities.SaleFilterActivity

import com.example.qraviaryapp.adapter.MyFirebaseMessagingService
import com.example.qraviaryapp.databinding.ActivityNavHomeBinding
import com.example.qraviaryapp.fragments.NavFragments.*
import com.example.qraviaryapp.fragments.ScanFragment
import com.example.qraviaryapp.fragments.SettingsFragment
import com.example.qraviaryapp.monitoring.MonitoringFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class NavHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityNavHomeBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var menu: Menu
    private var currentFragment: Fragment? = null
    private var lastBackPressTime: Long = 0
    private lateinit var navigationView: NavigationView
    lateinit var incubating: TextView
    lateinit var adulting: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var incubatingBadge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        binding = ActivityNavHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mAuth = FirebaseAuth.getInstance()
        val MyFirebaseMessagingService = MyFirebaseMessagingService()
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
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
//            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })

        incubating =
            MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.nav_incubating)) as TextView
        adulting =
            MenuItemCompat.getActionView(navigationView.menu.findItem(R.id.nav_aldulting)) as TextView

        initializeCountDrawer()

        checkElapsedTime()
    }

    private fun initializeCountDrawer() {
        val totalEggCount = sharedPreferences.getInt("totalEggCount", 0)
        incubating.gravity = Gravity.CENTER_VERTICAL
        incubating.setTypeface(null, Typeface.BOLD)
        incubating.setTextColor(resources.getColor(R.color.purpledark))
        incubating.text = totalEggCount.toString()
        adulting.gravity = Gravity.CENTER_VERTICAL
        adulting.setTypeface(null, Typeface.BOLD)
        adulting.setTextColor(resources.getColor(R.color.purpledark))
        adulting.text = ""
    }

    private fun checkElapsedTime() {
        val appStoppedTime = sharedPreferences.getLong("appStoppedTime", 0)
        val currentTimeMillis = System.currentTimeMillis()
        val elapsedTime = currentTimeMillis - appStoppedTime
        val thresholdTime = 60000 // 5 seconds in milliseconds

//        if (elapsedTime >= thresholdTime) {
//            // Show the splash activity
//            val intent = Intent(this, SplashActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }

    //    private fun initializeCountDrawer() {
//        // TODO: Implement the count initialization logic
//    }
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


    private lateinit var fragment: Fragment

    @SuppressLint("SuspiciousIndentation")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

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

            R.id.nav_scanner -> {
                fragment = ScanFragment()
                title = "QR Reader"
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

                isMonitoringFragment = true
            }

            R.id.nav_sales -> {
                fragment = NavSalesFragment()
                title = "Sales"
                isMonitoringFragment = true
            }

            R.id.nav_purchases -> {
                fragment = NavPurchasesFragment()
                title = "Purchases"
                isMonitoringFragment = true
            }

            R.id.nav_balance -> {
                fragment = BalanceFragment()
                title = "Balance"

                isMonitoringFragment = false
            }

//            R.id.nav_gallery -> {
//                fragment = GalleryFragment()
//                title = "Gallery"
//                isMonitoringFragment = false
//            }

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
                isMonitoringFragment = false
            }
        }
        if (!isMonitoringFragment) {
            toolbar.elevation = resources.getDimension(R.dimen.toolbar_elevation)
            supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        R.color.toolbarcolor
                    )
                )
            )

        } else {
            toolbar.elevation = 0f
            supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        R.color.toolbarcolor
                    )
                )
            )
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        }
        if (title == "Monitoring" || title == "Cages" || title == "Statistics" || title == "Mutations" || title == "Incubating" || title == "Adulting" || title == "Balance" ||
            title == "Categories" || title == "Settings" || title == "QR Reader"
        ) {
            hideMenuItems()
        } else {
            showMenuItems()
        }

        // Set the toolbar title
        replaceFragment(fragment)
        drawerLayout.closeDrawer(GravityCompat.START)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = title

        currentFragment = fragment
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
                // Determine the current fragment
                when (currentFragment) {
                    is BirdsFragment -> {
                        // Handle the ic_filter action for the BirdsFragment
                        val requestCode = 0
                        val intent = Intent(this, BirdFilterActivity::class.java)
                        startActivityForResult(intent, requestCode)
                    }

                    is PairsFragment -> {
                        // Handle the ic_filter action for the BirdsFragment
                        val intent = Intent(this, PairFilteringActivity::class.java)
                        startActivity(intent)
                    }

                    is NavSalesFragment -> {
                        // Handle the ic_filter action for the BirdsFragment
                        val requestCode = 1
                        val intent = Intent(this, SaleFilterActivity::class.java)
                        startActivityForResult(intent, requestCode)
                    }

                    is NavPurchasesFragment -> {
                        val requestCode = 2
                        // Handle the ic_filter action for the BirdsFragment
                        val intent = Intent(this, PurchaseFilterActivity::class.java)
                        startActivityForResult(intent, requestCode)
                    }

                    // Add more cases for other fragments as needed
                    else -> {
                        val requestCode = 3
                        val intent = Intent(this, ExpensesFilterActivity::class.java)
                        startActivityForResult(intent, requestCode)
                    }
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                val selectedStatus = data?.getStringArrayListExtra("selectedStatusList")
                val selectedGender = data?.getStringArrayListExtra("selectedGenderList")
                val selectedSort = data?.getStringExtra("selectedSort")
                val bundle = Bundle()

                bundle.putStringArrayList("selectedStatusList", selectedStatus)
                bundle.putStringArrayList("selectedGenderList", selectedGender)
                bundle.putString("selectedSort", selectedSort)


                fragment.arguments = bundle
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val fromDate = data?.getStringExtra("FromDate")
                val toDate = data?.getStringExtra("ToDate")
                val buyer = data?.getStringExtra("Buyer")
                val bundle = Bundle()
                Log.d(TAG, " From " + fromDate.toString())
                Log.d(TAG, " to " + toDate.toString())
                Log.d(TAG, " buyer " + buyer.toString())
                bundle.putString("FromDate", fromDate)
                bundle.putString("ToDate", toDate)
                bundle.putString("Buyer", buyer)


                fragment.arguments = bundle
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                val fromDate = data?.getStringExtra("FromDate")
                val toDate = data?.getStringExtra("ToDate")
                val buyer = data?.getStringExtra("Buyer")
                val bundle = Bundle()
                Log.d(TAG, " From " + fromDate.toString())
                Log.d(TAG, " to " + toDate.toString())
                Log.d(TAG, " buyer " + buyer.toString())
                bundle.putString("FromDate", fromDate)
                bundle.putString("ToDate", toDate)
                bundle.putString("Buyer", buyer)


                fragment.arguments = bundle
            }
        }

        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                val fromDate = data?.getStringExtra("FromDate")
                val toDate = data?.getStringExtra("ToDate")
                val minimum = data?.getStringExtra("minimum")
                val maximum = data?.getStringExtra("maximum")
                val categories = data?.getStringArrayListExtra("checkedCategory")
                val bundle = Bundle()
                Log.d(TAG, " From " + fromDate.toString())
                Log.d(TAG, " to " + toDate.toString())
                Log.d(TAG, " Categories " + categories.toString())
                bundle.putString("FromDate", fromDate)
                bundle.putString("ToDate", toDate)
                bundle.putString("minimum", minimum)
                bundle.putString("maximum", maximum)
                bundle.putStringArrayList("checkedCategory", categories)


                fragment.arguments = bundle
            }
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
            if (isAnyAccountOccupyingASlot()) {
                startActivity(Intent(this, SaveLoginActivity::class.java))
                this.finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }

        }
    }

    fun isAnyAccountOccupyingASlot(): Boolean {
        val maxAccounts = 4
        for (i in 1..maxAccounts) {
            val userKey = "user$i"
            if (sharedPreferences.contains(userKey)) {
                return true
            }
        }
        return false
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val doublePressInterval = 2000 // 2 seconds

        if (currentTime - lastBackPressTime < doublePressInterval) {
            super.onBackPressed() // Close the app
        } else {
            lastBackPressTime = currentTime
            val message = when (currentFragment) {
                is BirdsFragment -> "Bird Page"
                is PairsFragment -> "Pairs Page"
                is CagesFragment -> "Cages Page"
                is StatisticsFragment -> "Statistics Page"
                is MutationsFragment -> "Mutations Page"
                is ScanFragment -> "QR Reader Page"
                is AdultingFragment -> "Adulting Page"
                is ExpensesFragment -> "Expenses Page"
                is NavSalesFragment -> "Sales Page"
                is NavPurchasesFragment -> "Purchases Page"
                is BalanceFragment -> "Balance Page"
                is GalleryFragment -> "Gallery Page"
                is IncubatingFragment -> "Incubating Page"
                is SettingsFragment -> "Settings Page"
                is CategoriesFragment -> "Categories Page"
                else -> "Home Page"
            }

            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
        // If you want to navigate back to the default fragment when the back button is pressed on the default fragment,
        // you can use the following code:
        if (currentFragment is MonitoringFragment) {
            // Navigate to the default fragment (e.g., MonitoringFragment)
            replaceFragment(MonitoringFragment())
        }
    }

}