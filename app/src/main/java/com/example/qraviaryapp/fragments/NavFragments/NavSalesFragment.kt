package com.example.qraviaryapp.fragments.NavFragments

import ExpensesData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddExpensesActivity
import com.example.qraviaryapp.adapter.DetailedAdapter.ExpensesAdapter
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.DetailedFragment.BirdBasicFragment
import com.example.qraviaryapp.fragments.DetailedFragment.BirdGalleryFragment
import com.example.qraviaryapp.fragments.Expenses.ExpensesChartFragment
import com.example.qraviaryapp.fragments.Expenses.SalesChartFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NavSalesFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var fragmentAdapter: FragmentAdapter
    //    private lateinit var dataList: ArrayList<ExpensesData>
//    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    //    private lateinit var adapter: ExpensesAdapter
//    private lateinit var fab: FloatingActionButton
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var totalBirds: TextView
    private var expensesCount = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nav_sales, container, false)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Set up NetworkCallback to detect network changes
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (!isNetworkAvailable) {
                    // Network was restored from offline, show Snackbar
                    showSnackbar("Your Internet connection was restored")
                }
                isNetworkAvailable = true
            }


            override fun onLost(network: Network) {
                super.onLost(network)
                // Network is offline, show Snackbar
                showSnackbar("You are currently offline")
                isNetworkAvailable = false
            }
        }

        // Register the NetworkCallback
        connectivityManager.registerDefaultNetworkCallback(networkCallback)

        fragmentAdapter = FragmentAdapter(childFragmentManager)
        val expensesFragment = SalesFragment()
        val chartFragment = SalesChartFragment()
        viewPager = view.findViewById(R.id.viewPager)
        tablayout = view.findViewById(R.id.tablayout)

        fragmentAdapter.addFragment(expensesFragment, "SALES")
        fragmentAdapter.addFragment(chartFragment, "CHART")
        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
        return view
    }
    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }

}