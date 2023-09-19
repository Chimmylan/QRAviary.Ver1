package com.example.qraviaryapp.activities.mainactivities


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.databinding.ActivityHomeBinding
import com.example.qraviaryapp.fragments.HomeFragment
import com.example.qraviaryapp.fragments.NotificationFragment
import com.example.qraviaryapp.fragments.ScanFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding : ActivityHomeBinding
    private lateinit var gso: GoogleSignInOptions
    private lateinit  var gsc: GoogleSignInClient

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var bottomappbar: BottomAppBar
    private lateinit var fabbutton: FloatingActionButton
    private lateinit  var nestedScrollView: NestedScrollView
    companion object {
        private const val RC_SIGN_IN = 1000
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView = findViewById(com.example.qraviaryapp.R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
        bottomappbar = findViewById(com.example.qraviaryapp.R.id.bottom_app_bar)
        bottomNavigationView.visibility = View.VISIBLE
        fabbutton = findViewById(com.example.qraviaryapp.R.id.fab_add)
        fabbutton.visibility = View.VISIBLE
        mAuth = FirebaseAuth.getInstance()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso)

        fabbutton.setOnClickListener {
            val fragmentManager = supportFragmentManager // Replace 'supportFragmentManager' with the appropriate FragmentManager instance

            fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, ScanFragment())
                .commit()

        }

        val account = GoogleSignIn.getLastSignedInAccount(this)

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){

                com.example.qraviaryapp.R.id.home -> replaceFragment(HomeFragment())
                com.example.qraviaryapp.R.id.notification -> replaceFragment(NotificationFragment())

                else ->{
                }
            }
            true
        }
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }
//        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            if (scrollY > oldScrollY) {
//                // Scrolling down, hide the bottom navigation view
//                bottomNavigationView.visibility = View.GONE
//            } else {
//                // Scrolling up, show the bottom navigation view
//                bottomNavigationView.visibility = View.VISIBLE
//            }
//        })

    }

    fun hideBottomNavigation() {
        bottomNavigationView.visibility = View.GONE
        bottomappbar.visibility = View.GONE
        fabbutton.visibility = View.GONE
    }

    fun showBottomNavigation() {
        bottomNavigationView.visibility = View.VISIBLE
        bottomappbar.visibility = View.VISIBLE
        fabbutton.visibility = View.VISIBLE
    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    fun signOut() {
        gsc.signOut().addOnCompleteListener {
            finish()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        }
        mAuth.signOut()
    }



    override fun onBackPressed() {
//        Toast.makeText(this@HomeActivity, "Back to Exit Again", Toast.LENGTH_LONG).show()
    }
}
