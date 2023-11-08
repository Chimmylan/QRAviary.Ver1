package com.example.qraviaryapp.fragments.NavFragments

import BirdData
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.CagesActivity.CagesAdapter.NurseryListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdultingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdultingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dataList: ArrayList<BirdData>
    private lateinit var adapter: NurseryListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var totalBirds: TextView
    private lateinit var loadingProgressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_adulting, container, false)
        /*  snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
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

  */

        val sharedPrefs: SharedPreferences? =
            context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val maturingValue =
            sharedPrefs?.getString("maturingValue", "50") // Default to 50 if not set
        val maturingDays = maturingValue?.toIntOrNull() ?: 50
        loadingProgressBar = rootView.findViewById(R.id.loadingProgressBar)
        totalBirds = rootView.findViewById(R.id.tvBirdCount)
        swipeToRefresh = rootView.findViewById(R.id.swipeToRefresh)
        recyclerView = rootView.findViewById(R.id.RecyclerView)
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.layoutManager = gridLayoutManager
        dataList = ArrayList()
        adapter = NurseryListAdapter(requireContext(), dataList, maturingDays)
        recyclerView.adapter = adapter

        mAuth = FirebaseAuth.getInstance()
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        refreshApp()
        return rootView
    }
    private fun refreshApp() {
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                try {

                    val data = getDataFromDatabase()
                    dataList.clear()
                    dataList.addAll(data)
                    swipeToRefresh.isRefreshing = false

                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
                }

            }
            Toast.makeText(requireContext(), "Refreshed", Toast.LENGTH_SHORT).show()
        }
    }
    private suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Cages")
            .child("Nursery Cages")
        val dataList = ArrayList<BirdData>()
        val snapshot = db.get().await()

        for (NurseryCages in snapshot.children) {
            val CageKey = NurseryCages.key

            val birds = NurseryCages.child("Birds")
            for (itemsnapshot in birds.children) {

                val data = itemsnapshot.getValue(BirdData::class.java)
                val gallery = itemsnapshot.child("Gallery")
                if (data != null) {

                    val mainPic = gallery.children.firstOrNull()?.value.toString()

                    val adultingKey = itemsnapshot.key
                    val nurserykey = itemsnapshot.child("Nursery Key").value.toString()
                    val birdKey = itemsnapshot.child("Bird Key").value
                    val LegbandValue = itemsnapshot.child("Legband").value
                    val identifierValue = itemsnapshot.child("Identifier").value
                    val genderValue = itemsnapshot.child("Gender").value
                    val mutation1Value = if (itemsnapshot.hasChild("Mutation1")) {
                        itemsnapshot.child("Mutation1").child("Mutation Name").value.toString()
                    } else {
                        ""
                    }
                    val mutation2Value = if (itemsnapshot.hasChild("Mutation2")) {
                        itemsnapshot.child("Mutation2").child("Mutation Name").value.toString()
                    } else {
                        ""
                    }
                    val mutation3Value = if (itemsnapshot.hasChild("Mutation3")) {
                        itemsnapshot.child("Mutation3").child("Mutation Name").value.toString()
                    } else {
                        ""
                    }
                    val mutation4Value = if (itemsnapshot.hasChild("Mutation4")) {
                        itemsnapshot.child("Mutation4").child("Mutation Name").value.toString()
                    } else {
                        ""
                    }
                    val mutation5Value = if (itemsnapshot.hasChild("Mutation5")) {
                        itemsnapshot.child("Mutation5").child("Mutation Name").value.toString()
                    } else {
                        ""
                    }
                    val mutation6Value = if (itemsnapshot.hasChild("Mutation6")) {
                        itemsnapshot.child("Mutation6").child("Mutation Name").value.toString()
                    } else {
                        ""
                    }
                    val maturingDays = itemsnapshot.child("Maturing Days").value.toString()

                    val dateOfBandingValue = itemsnapshot.child("Date of Banding").value
                    val dateOfBirthValue = itemsnapshot.child("Date of Birth").value
                    val statusValue = itemsnapshot.child("Status").value
                    val availCageValue = itemsnapshot.child("Cage").value
                    val forSaleCageValue = itemsnapshot.child("Cage").value
                    val forSaleRequestedPriceValue = itemsnapshot.child("Requested Price").value
                    val soldDateValue = itemsnapshot.child("Sold Date").value
                    val soldPriceValue = itemsnapshot.child("Sale Price").value
                    val soldContactValue = itemsnapshot.child("Sale Contact").value
                    val deathDateValue = itemsnapshot.child("Death Date").value
                    val deathReasonValue = itemsnapshot.child("Death Reason").value
                    val exDateValue = itemsnapshot.child("Exchange Date").value
                    val exReasonValue = itemsnapshot.child("Exchange Reason").value
                    val exContactValue = itemsnapshot.child("Exchange Contact").value
                    val lostDateValue = itemsnapshot.child("Lost Date").value
                    val lostDetailsValue = itemsnapshot.child("Lost Details").value
                    val donatedDateValue = itemsnapshot.child("Donated Date").value
                    val donatedContactValue = itemsnapshot.child("Donated Contact").value
                    val otherCommentsValue = itemsnapshot.child("Comments").value
                    val buyPriceValue = itemsnapshot.child("Buy Price").value
                    val boughtDateValue = itemsnapshot.child("Bought Date").value
                    val breederContactValue = itemsnapshot.child("Breeder Contact").value
                    val FatherValue = itemsnapshot.child("Parents").child("Father").value
                    val MotherValue = itemsnapshot.child("Parents").child("Mother").value
                    val fatherKeyValue = itemsnapshot.child("Parents").child("FatherKey").value
                    val motherKeyValue = itemsnapshot.child("Parents").child("MotherKey").value

                    val legband = LegbandValue.toString() ?: ""
                    val identifier = identifierValue.toString() ?: ""
                    val gender = genderValue.toString() ?: ""
                    val dateOfBanding = dateOfBandingValue.toString() ?: ""
                    val dateOfBirth = dateOfBirthValue.toString() ?: ""
                    val status = statusValue.toString() ?: ""
                    val availCage = availCageValue.toString() ?: ""
                    val forSaleCage = forSaleCageValue.toString() ?: ""
                    val forSaleRequestedPrice = forSaleRequestedPriceValue.toString() ?: ""
                    val soldDate = soldDateValue.toString() ?: ""
                    val soldPrice = soldPriceValue.toString() ?: ""
                    val soldContact = soldContactValue.toString() ?: ""
                    val deathDate = deathDateValue.toString() ?: ""
                    val deathReason = deathReasonValue.toString() ?: ""
                    val exDate = exDateValue.toString() ?: ""
                    val exReason = exReasonValue.toString() ?: ""
                    val exContact = exContactValue.toString() ?: ""
                    val lostDate = lostDateValue.toString() ?: ""
                    val lostDetails = lostDetailsValue.toString() ?: ""
                    val donatedDate = donatedDateValue.toString() ?: ""
                    val donatedContact = donatedContactValue.toString() ?: ""
                    val otherComments = otherCommentsValue.toString() ?: ""
                    val buyPrice = buyPriceValue.toString() ?: ""
                    val boughtDate = boughtDateValue.toString() ?: ""
                    val boughtBreeder = breederContactValue.toString() ?: ""
                    val mother = MotherValue.toString() ?: ""
                    val father = FatherValue.toString() ?: ""
                    data.maturingDays = maturingDays
                    data.adultingKey = adultingKey
                    data.cageKey = CageKey
                    data.img = mainPic
                    data.birdKey = birdKey.toString()
                    data.nurseryKey = nurserykey
                    data.legband = legband
                    data.identifier = identifier
                    data.gender = gender
                    data.dateOfBanding = dateOfBanding
                    data.dateOfBirth = dateOfBirth
                    data.status = status
                    data.mutation1 = mutation1Value
                    data.mutation2 = mutation2Value
                    data.mutation3 = mutation3Value
                    data.mutation4 = mutation4Value
                    data.mutation5 = mutation5Value
                    data.mutation6 = mutation6Value
                    data.availCage = availCage
                    data.forSaleCage = forSaleCage
                    data.reqPrice = forSaleRequestedPrice
                    data.soldDate = soldDate
                    data.soldPrice = soldPrice
                    data.saleContact = soldContact
                    data.deathDate = deathDate
                    data.deathReason = deathReason
                    data.exDate = exDate
                    data.exReason = exReason
                    data.exContact = exContact
                    data.lostDate = lostDate
                    data.lostDetails = lostDetails
                    data.donatedDate = donatedDate
                    data.donatedContact = donatedContact
                    data.otherComments = otherComments
                    data.buyPrice = buyPrice
                    data.boughtDate = boughtDate
                    data.breederContact = boughtBreeder
                    data.father = father
                    data.mother = mother
                    data.fatherKey = fatherKeyValue.toString()
                    data.motherKey = motherKeyValue.toString()

                    if (Looper.myLooper() != Looper.getMainLooper()) {
                        Log.d(ContentValues.TAG, "Code is running on a background thread")
                    } else {
                        Log.d(ContentValues.TAG, "Code is running on the main thread")
                        //
                    }
                    dataList.add(data)


                }

            }
        }

        if(dataList.count()>1){
            totalBirds.text = dataList.count().toString() + " Birds"
        }
        else{
            totalBirds.text = dataList.count().toString() + " Bird"
        }
        dataList

    }
    override fun onResume() {
        super.onResume()

        // Call a function to reload data from the database and update the RecyclerView
        reloadDataFromDatabase()

    }

    private fun reloadDataFromDatabase() {
        loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {

                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)

                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            } finally {

                loadingProgressBar.visibility = View.GONE
            }
        }}
    /* private fun showSnackbar(message: String) {
         snackbar.setText(message)
         snackbar.show()
     }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdultingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdultingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}