package com.example.qraviaryapp.fragments.NavFragments

import BirdData
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.ArraySet
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.AddBirdActivity
import com.example.qraviaryapp.activities.AddActivities.AddBirdFlightActivity
import com.example.qraviaryapp.adapter.BirdListAdapter
import com.example.qraviaryapp.adapter.StickyHeaderItemDecorationbirdlist
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Locale

class BirdsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var dataList: ArrayList<BirdData>
    private lateinit var adapter: BirdListAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var totalBirds: TextView
    private var status = ArraySet<String>()
    private var birdCount = 0
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_birds, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor =
                ContextCompat.getColor(requireContext(), R.color.totalbirds)
        }
        swipeToRefresh = rootView.findViewById(R.id.swipeToRefresh)
        loadingProgressBar = rootView.findViewById(R.id.loadingProgressBar)
        totalBirds = rootView.findViewById(R.id.tvBirdCount)
        fab = rootView.findViewById(R.id.fab)
        recyclerView = rootView.findViewById(R.id.recyclerView_bird_list)

//        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
//        recyclerView.layoutManager = gridLayoutManager
//        dataList = ArrayList()
//        adapter = BirdListAdapter(requireContext(), dataList)
//        recyclerView.adapter = adapter
        dataList = ArrayList()
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BirdListAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(StickyHeaderItemDecorationbirdlist(adapter))
        mAuth = FirebaseAuth.getInstance()


        sharedPreferences =
            requireContext().getSharedPreferences("BirdFilter", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()


        lifecycleScope.launch {
            try {
//                loadingProgressBar.visibility = View.VISIBLE
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)


                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            }
//            finally {
//                // Hide the loading ProgressBar when reloading finishes
//                loadingProgressBar.visibility = View.GONE
//            }
        }
        fab.setOnClickListener {
            showOptionDialog()
        }

        snackbar = Snackbar.make(rootView, "", Snackbar.LENGTH_LONG)
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

        refreshApp()


        return rootView
    }

    private fun refreshApp() {
        swipeToRefresh.setOnRefreshListener {
            lifecycleScope.launch(Dispatchers.Main) {
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


    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }

    /*private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }*/
    private suspend fun getDataFromDatabase(): List<BirdData> = withContext(Dispatchers.IO) {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().getReference("Users")
            .child("ID: ${currentUserId.toString()}").child("Birds")
        val dataList = ArrayList<BirdData>()
        val snapshot = db.get().await()

        for (itemSnapshot in snapshot.children) {

            val data = itemSnapshot.getValue(BirdData::class.java)
            val gallery = itemSnapshot.child("Gallery")
            if (data != null) {

                val mainPic = gallery.children.firstOrNull()?.value.toString()
//                val imageUrl = "$mainPic?timestamp=${System.currentTimeMillis()}"
                val flightKey = itemSnapshot.child("Flight Key").value.toString()
                val birdKey = itemSnapshot.key
                val LegbandValue = itemSnapshot.child("Legband").value
                val identifierValue = itemSnapshot.child("Identifier").value
                val genderValue = itemSnapshot.child("Gender").value

                val mutation1Value = if (itemSnapshot.hasChild("Mutation1")) {
                    itemSnapshot.child("Mutation1").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation2Value = if (itemSnapshot.hasChild("Mutation2")) {
                    itemSnapshot.child("Mutation2").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation3Value = if (itemSnapshot.hasChild("Mutation3")) {
                    itemSnapshot.child("Mutation3").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation4Value = if (itemSnapshot.hasChild("Mutation4")) {
                    itemSnapshot.child("Mutation4").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation5Value = if (itemSnapshot.hasChild("Mutation5")) {
                    itemSnapshot.child("Mutation5").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val mutation6Value = if (itemSnapshot.hasChild("Mutation6")) {
                    itemSnapshot.child("Mutation6").child("Mutation Name").value.toString()
                } else {
                    ""
                }
                val dateOfBandingValue = itemSnapshot.child("Date of Banding").value
                val dateOfBirthValue = itemSnapshot.child("Date of Birth").value
                val statusValue = itemSnapshot.child("Status").value
                val availCageValue = itemSnapshot.child("Cage").value
                val forSaleCageValue = itemSnapshot.child("Cage").value
                val cageKey = itemSnapshot.child("CageKey").value
                val forSaleRequestedPriceValue = itemSnapshot.child("Requested Price").value
                val soldDateValue = itemSnapshot.child("Sold Date").value
                val soldPriceValue = itemSnapshot.child("Sale Price").value
                val soldContactValue = itemSnapshot.child("Sale Contact").value
                val deathDateValue = itemSnapshot.child("Death Date").value
                val deathReasonValue = itemSnapshot.child("Death Reason").value
                val exDateValue = itemSnapshot.child("Exchange Date").value
                val exReasonValue = itemSnapshot.child("Exchange Reason").value
                val exContactValue = itemSnapshot.child("Exchange Contact").value
                val lostDateValue = itemSnapshot.child("Lost Date").value
                val lostDetailsValue = itemSnapshot.child("Lost Details").value
                val donatedDateValue = itemSnapshot.child("Donated Date").value
                val donatedContactValue = itemSnapshot.child("Donated Contact").value
                val otherCommentsValue = itemSnapshot.child("Comments").value
                val buyPriceValue = itemSnapshot.child("Buy Price").value
                val boughtDateValue = itemSnapshot.child("Bought Date").value
                val breederContactValue = itemSnapshot.child("Breeder Contact").value
                val FatherValue = itemSnapshot.child("Parents").child("Father").value
                val MotherValue = itemSnapshot.child("Parents").child("Mother").value
                val fatherKeyValue = itemSnapshot.child("Parents").child("FatherKey").value
                val motherKeyValue = itemSnapshot.child("Parents").child("MotherKey").value
                val nurseryType = itemSnapshot.child("Nursery Key").value.toString()
                /*==++==*/
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

//                val image = getUrlImage(imageUrl)

                birdCount++
//                data.bitmap = image
                data.nurseryType = nurseryType
                data.flightType = flightKey
                data.cageKey = cageKey.toString()
                data.img = mainPic
                data.birdCount = birdCount.toString()
                data.birdKey = birdKey
                data.flightKey = flightKey
                data.legband = legband
                data.identifier = identifier
                data.gender = gender
                data.dateOfBanding = dateOfBanding
                data.dateOfBirth = dateOfBirth
                data.year = extractYearFromDateString(dateOfBirth)
                data.month = extractYearFromDate(dateOfBirth)
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


        if (dataList.count() > 1) {
            totalBirds.text = dataList.count().toString() + " Birds"
        } else {
            totalBirds.text = dataList.count().toString() + " Bird"
        }

        dataList.sortByDescending { it.year?.substring(0, 4)?.toIntOrNull() ?: 0 }
        dataList


    }


    private fun extractYearFromDateString(dateString: String): String {
        val date = SimpleDateFormat("MMM d yyyy", Locale.getDefault()).parse(dateString)
        return date?.let { SimpleDateFormat("yyyy", Locale.getDefault()).format(it) } ?: ""
    }

    private fun extractYearFromDate(dateString: String): String {
        val date = SimpleDateFormat("MMM d yyyy", Locale.getDefault()).parse(dateString)
        return date?.let { SimpleDateFormat("yyyy MM d", Locale.getDefault()).format(it) } ?: ""
    }

    fun getUrlImage(urlString: String): Bitmap? {
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null

        try {
            val url = URL(urlString)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            inputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return bitmap
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed() // Navigate back to the previous fragment
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showOptionDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val popUp = inflater.inflate(R.layout.dialog_addbird_option, null)
        val btnNursery = popUp.findViewById<MaterialButton>(R.id.btnNursery)
        val btnFlight = popUp.findViewById<MaterialButton>(R.id.btnFlight)

        builder.setTitle("Add Bird Selection")
        builder.setView(popUp)
        val alertDialog = builder.create()

        btnNursery.setOnClickListener {
            val i = Intent(requireContext(), AddBirdActivity::class.java)
            startActivity(i)
            alertDialog.dismiss()
        }

        btnFlight.setOnClickListener {
            val i = Intent(requireContext(), AddBirdFlightActivity::class.java)
            startActivity(i)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()

        // Call a function to reload data from the database and update the RecyclerView

        //sharedpref


        //category
        reloadDataFromDatabase()

//                adapter.filterAge(selectedSortBy)


    }

    private fun reloadDataFromDatabase() {
        loadingProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {

                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)

                filterdata()

                adapter.notifyDataSetChanged()

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error reloading data: ${e.message}")
            } finally {

                loadingProgressBar.visibility = View.GONE
            }
        }
    }

    fun filterdata(){

        val sharedPreferencesFileName = "BirdFilter"
        val sharedPreferencesFile = File("/data/data/com.example.qraviaryapp/shared_prefs/$sharedPreferencesFileName.xml")


        if (!sharedPreferencesFile.exists()){
            editor.putString("category_Available", "Available")
            editor.putString("category_For Sale", "For Sale")
            editor.putString("category_Paired", "Paired")
            editor.putString("gender_Unknown", "Unknown")
            editor.putString("category_Male", "Male")
            editor.putString("category_Female", "Female")
            editor.putString("sort", "Youngest")
            editor.apply()
        }

        val paired = sharedPreferences.getString("category_Paired", "")
        val forSale = sharedPreferences.getString("category_For Sale", "")
        val deceased = sharedPreferences.getString("category_Deceased", "")
        val lost = sharedPreferences.getString("category_Lost", "")
        val sold = sharedPreferences.getString("category_Sold", "")
        val exchange = sharedPreferences.getString("category_Exchanged", "")
        val available = sharedPreferences.getString("category_Available", "")
        val donated = sharedPreferences.getString("category_Donated", "")
        val other = sharedPreferences.getString("category_Other", "")

        //gender
        val unknown = sharedPreferences.getString("gender_Unknown", "")
        val male = sharedPreferences.getString("gender_Male", "")
        val female = sharedPreferences.getString("gender_Female", "")

        //age

        val sort = sharedPreferences.getString("Sort","Youngest")


        val categoryFilters: Set<String> = setOf(
            paired,
            forSale,
            deceased,
            lost,
            sold,
            exchange,
            available,
            donated,
            other
        ).filter { it?.isNotBlank()!! }.toSet() as Set<String>

        val genderFilters: Set<String> = setOf(
            unknown,
            male,
            female
        ).filter { it?.isNotEmpty()!! }.toSet() as Set<String>

        var filteredData: Map<String, Set<String>> = mapOf(
            "status" to categoryFilters,
            "gender" to genderFilters
        )

        Log.d(TAG, filteredData.toString())


        if (sort != null) {
            adapter.filterData(filteredData, sort)
        }
    }
}
