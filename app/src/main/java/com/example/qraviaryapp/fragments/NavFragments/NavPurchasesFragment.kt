package com.example.qraviaryapp.fragments.NavFragments

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.example.qraviaryapp.fragments.Expenses.PurchaseChartFragment
import com.example.qraviaryapp.fragments.Expenses.SalesChartFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NavPurchasesFragment : Fragment() {
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

    private var toDate: String? = null
    private var fromDate: String? = null
    private var buyer: String? = null

    private lateinit var expensesFragment: PurchasesFragment
    private lateinit var chartFragment: PurchaseChartFragment
    private var bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nav_purchases, container, false)

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
        expensesFragment = PurchasesFragment()
        chartFragment = PurchaseChartFragment()
        viewPager = view.findViewById(R.id.viewPager)
        tablayout = view.findViewById(R.id.tablayout)

        fragmentAdapter.addFragment(expensesFragment, "PURCHASES")
        fragmentAdapter.addFragment(chartFragment, "CHART")
        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)
        return view
    }
    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()

        fromDate = arguments?.getString("FromDate")
        toDate = arguments?.getString("ToDate")
        buyer = arguments?.getString("Buyer")


        Log.d(ContentValues.TAG,"NAVSALES" + fromDate.toString())

        bundle.putString("FromDate", fromDate)
        bundle.putString("ToDate", toDate)
        bundle.putString("Buyer", buyer)

        expensesFragment.arguments = bundle

    }

}