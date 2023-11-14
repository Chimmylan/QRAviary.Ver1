package com.example.qraviaryapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.R.layout
import com.example.qraviaryapp.R.menu
import com.example.qraviaryapp.activities.mainactivities.HomeActivity
import com.example.qraviaryapp.activities.mainactivities.LoginActivity
import com.example.qraviaryapp.activities.mainactivities.SettingsActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var options: TextView
    private lateinit var birdcv: CardView
    private lateinit var cagescv: CardView
    private lateinit var pairscv: CardView
    private lateinit var salescv: CardView
    private lateinit var speciescv: CardView
    private lateinit var expensescv: CardView
    private lateinit var statisticscv: CardView
    private lateinit var balancecv: CardView
    private lateinit var incubatingcv: CardView
    private lateinit var maturingcv: CardView
    private lateinit var tempandhumicv: CardView
    private lateinit var set1: CardView
    private lateinit var set2: CardView
    private lateinit var pbbird : ProgressBar
    private lateinit var pbpair : ProgressBar
    private lateinit var pbcage : ProgressBar
    private lateinit var pbstatistic : ProgressBar
    private lateinit var pbmutations : ProgressBar
    private lateinit var pbincubating : ProgressBar
    private lateinit var pbmaturing : ProgressBar
    private lateinit var pbexpenses : ProgressBar
    private lateinit var pbsales : ProgressBar
    private lateinit var pbbalance : ProgressBar
    private lateinit var pbtemperature : ProgressBar
    private lateinit var pbhumidity : ProgressBar
    private lateinit var pbtempandhumid : ProgressBar
    private lateinit var homeActivity: HomeActivity
    private lateinit var mAuth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit  var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is HomeActivity) {
            homeActivity = context
        } else {
            throw ClassCastException("Parent activity must be HomeActivity")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_home, container, false)
        options = view.findViewById(R.id.options)
        birdcv = view.findViewById(R.id.birdcv)
        pairscv = view.findViewById(R.id.paircv)
        cagescv = view.findViewById(R.id.cagescv)
        speciescv = view.findViewById(R.id.speciescv)
        balancecv = view.findViewById(R.id.balancecv)
        statisticscv = view.findViewById(R.id.statisticscv)
        salescv = view.findViewById(R.id.salescv)
        expensescv = view.findViewById(R.id.expensescv)
        incubatingcv = view.findViewById(R.id.incubatingcv)
        maturingcv = view.findViewById(R.id.maturingcv)
        tempandhumicv = view.findViewById(R.id.tempandhumicv)

        set1 = view.findViewById(R.id.set1)
        set2 = view.findViewById(R.id.set2)
        pbtemperature = view.findViewById(R.id.pbtemperature)
        pbhumidity = view.findViewById(R.id.pbhumidity)
        pbbird = view.findViewById(R.id.pbbird)
        pbpair = view.findViewById(R.id.pbpair)
        pbcage = view.findViewById(R.id.pbcage)
        pbstatistic = view.findViewById(R.id.pbstatistic)
        pbmutations = view.findViewById(R.id.pbmutation)
        pbincubating = view.findViewById(R.id.pbincubating)
        pbmaturing = view.findViewById(R.id.pbmaturing)
        pbexpenses = view.findViewById(R.id.pbexpenses)
        pbsales = view.findViewById(R.id.pbsales)
        pbbalance = view.findViewById(R.id.pbbalance)
        pbtempandhumid = view.findViewById(R.id.pbtempandhumid)


        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)

        mAuth = FirebaseAuth.getInstance()

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)


        nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > oldScrollY) {
                // Scrolling downwards, hide the bottom navigation
             //   homeActivity.hideBottomNavigation()
            } else {
                // Scrolling upwards or reached the top, show the bottom navigation
             //   homeActivity.showBottomNavigation()
            }
        }

        // Example code to show the bottom navigation initially
        homeActivity.showBottomNavigation()


        /*birdcv.setOnClickListener {
            showProgressBar(pbbird)
            Handler().postDelayed({
                hideProgressBar(pbbird)
                val intent = Intent(requireContext(), BirdListActivity::class.java)
                startActivity(intent)
                true

            }, 1000)
        }

        pairscv.setOnClickListener {
            showProgressBar(pbpair)
            Handler().postDelayed({
                hideProgressBar(pbpair)
                val intent = Intent(requireContext(), PairListActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }

        cagescv.setOnClickListener {
            showProgressBar(pbcage)
            Handler().postDelayed({
                hideProgressBar(pbcage)
                val intent = Intent(requireContext(), CageListActivity2::class.java)
                startActivity(intent)
                true
            }, 1000)
        }
        statisticscv.setOnClickListener {
            showProgressBar(pbstatistic)
            Handler().postDelayed({
                hideProgressBar(pbstatistic)
                val intent = Intent(requireContext(), StatisticsActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }
        speciescv.setOnClickListener {
            showProgressBar(pbmutations)
            Handler().postDelayed({
                hideProgressBar(pbmutations)

                val intent = Intent(requireContext(), HomeMutationActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }

        salescv.setOnClickListener {
            showProgressBar(pbsales)
            Handler().postDelayed({
                hideProgressBar(pbsales)
                val intent = Intent(requireContext(), SalesPurchasesActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }

        expensescv.setOnClickListener {
            showProgressBar(pbexpenses)
            Handler().postDelayed({
                hideProgressBar(pbexpenses)
                val intent = Intent(requireContext(), ExpensesActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }

        incubatingcv.setOnClickListener {
            showProgressBar(pbincubating)
            Handler().postDelayed({
                hideProgressBar(pbincubating)
                val intent = Intent(requireContext(), IncubatingActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }

        maturingcv.setOnClickListener {
            showProgressBar(pbmaturing)
            Handler().postDelayed({
                hideProgressBar(pbmaturing)
                val intent = Intent(requireContext(), MaturingActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }
        balancecv.setOnClickListener {
            showProgressBar(pbbalance)
            Handler().postDelayed({
                hideProgressBar(pbbalance)
                val intent = Intent(requireContext(), NavHomeActivity::class.java)
                startActivity(intent)
                true
            }, 1000)
        }
        options.setOnClickListener {
            showPopupMenu(options)
        }
        tempandhumicv.setOnClickListener {
            showProgressBar(pbtempandhumid)
            Handler().postDelayed({
                hideProgressBar(pbtempandhumid)
            }, 1000)
        }
        set1.setOnClickListener {
            showProgressBar(pbtemperature)
            Handler().postDelayed({
                hideProgressBar(pbtemperature)
            }, 1000)
        }
        set2.setOnClickListener {
            showProgressBar(pbhumidity)
            Handler().postDelayed({
                hideProgressBar(pbhumidity)
            }, 1000)
        }*/
        return view

    }
    /*private fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.INVISIBLE
    }*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
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
   private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(menu.home_menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_settings -> {
                    val intent = Intent(requireContext(), SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    showLogoutConfirmationDialog()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
    fun signOut() {
        mAuth.signOut()
        gsc.signOut().addOnCompleteListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
