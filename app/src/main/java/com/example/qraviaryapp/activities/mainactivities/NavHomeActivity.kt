package com.example.qraviaryapp.activities.mainactivities

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.databinding.ActivityNavHomeBinding
import com.example.qraviaryapp.fragments.NavFragments.BalanceFragment
import com.example.qraviaryapp.fragments.NavFragments.BirdsFragment
import com.example.qraviaryapp.fragments.NavFragments.CagesFragment
import com.example.qraviaryapp.fragments.NavFragments.ExpensesFragment
import com.example.qraviaryapp.fragments.NavFragments.IncubatingFragment
import com.example.qraviaryapp.monitoring.MonitoringFragment
import com.example.qraviaryapp.fragments.NavFragments.MutationsFragment
import com.example.qraviaryapp.fragments.NavFragments.PairsFragment
import com.example.qraviaryapp.fragments.NavFragments.PurchasesFragment
import com.example.qraviaryapp.fragments.NavFragments.SalesFragment
import com.example.qraviaryapp.fragments.NavFragments.StatisticsFragment
import com.example.qraviaryapp.fragments.SettingsFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class NavHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding : ActivityNavHomeBinding
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var gso: GoogleSignInOptions
    private lateinit  var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.bottom_nav_background)
        }
        binding = ActivityNavHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)
        /*toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)*/
        toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open_nav, R.string.close_nav)
            .apply {
                drawerLayout.addDrawerListener(this)
                syncState()
            }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, MonitoringFragment()).commit()
            navigationView.menu.getItem(0).isChecked = true
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: Fragment
        val title: String

        when (item.itemId) {
            R.id.nav_birds -> {
                fragment = BirdsFragment()
                title = "Birds"
            }
            R.id.nav_pairs -> {
                fragment = PairsFragment()
                title = "Pairs"
            }
            R.id.nav_cages -> {
                fragment = CagesFragment()
                title = "Cages"
            }
            R.id.nav_statistics -> {
                fragment = StatisticsFragment()
                title = "Statistics"
            }
            R.id.nav_mutations -> {
                fragment = MutationsFragment()
                title = "Mutations"
            }
            R.id.nav_aldulting -> {
                fragment = CagesFragment()
                title = "Adulting"
            }
            R.id.nav_expenses -> {
                fragment = ExpensesFragment()
                title = "Expenses"
            }
            R.id.nav_sales -> {
                fragment = SalesFragment()
                title = "Sales"
            }
            R.id.nav_purchases -> {
                fragment = PurchasesFragment()
                title = "Purchases"
            }
            R.id.nav_balance -> {
                fragment = BalanceFragment()
                title = "Balance"
            }
            R.id.nav_incubating -> {
                fragment = IncubatingFragment()
                title = "Incubating"
            }
            R.id.nav_settings -> {
                fragment = SettingsFragment()
                title = "Settings"
            }
            R.id.nav_logout -> {
                showLogoutConfirmationDialog()
                return true // Early return, no need to continue
            }
            else -> {
                fragment = MonitoringFragment() // Default fragment
                title = "Monitoring" // Default title
            }
        }



        // Set the toolbar title
        replaceFragment(fragment)
        drawerLayout.closeDrawer(GravityCompat.START)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = title
        return true
    }
   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }*/

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