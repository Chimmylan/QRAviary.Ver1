package com.example.qraviaryapp.fragments.Pairs

import EggData
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.ClutchesListAdapter
import com.example.qraviaryapp.adapter.MyAlarmReceiver
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClutchesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClutchesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tvDate: TextView
    private lateinit var tvMutations: TextView
    private lateinit var btnMale: MaterialButton
    private lateinit var btnFemale: MaterialButton
    private lateinit var fab: FloatingActionButton
    private lateinit var mAuth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClutchesListAdapter
    private lateinit var dataList: ArrayList<EggData>
    private lateinit var pairId: String
    private lateinit var pairKey: String
    private lateinit var pairMaleKey: String
    private lateinit var pairFlightMaleKey: String
    private lateinit var pairFemaleKey: String
    private lateinit var pairFlightFemaleKey: String
    private lateinit var pairMale: String
    private lateinit var pairFemale: String
    private lateinit var pairCageKeyMale: String
    private lateinit var pairCageKeyFemale: String
    private lateinit var pairCageBirdMale: String
    private lateinit var pairCageBirdFemale: String
    private lateinit var paircagekey: String
    private lateinit var cagePairKey: String
    private lateinit var currentUserId: String
    private lateinit var totalclutch: TextView
    private var clutchCount = 0
    private lateinit var clutchkey: String
    private var key: String = ""
    private lateinit var hatchingDateTime: LocalDateTime
    private var storageRef = Firebase.storage.reference
    private val formatter1 = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a", Locale.US)
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
        val view = inflater.inflate(R.layout.fragment_clutches, container, false)
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)
        totalBirds = view.findViewById(R.id.tvBirdCount)
        mAuth = FirebaseAuth.getInstance()
        dataList = ArrayList()
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = gridLayoutManager
        adapter = ClutchesListAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar)

        fab = view.findViewById(R.id.fab)
//        pairKey = arguments?.getString("BirdKey").toString()
        paircagekey = arguments?.getString("CageKey").toString()
        pairId = arguments?.getString("PairId").toString()
        pairMale = arguments?.getString("MaleID").toString()
        pairFemale = arguments?.getString("FemaleID").toString()
        val beginningDate = arguments?.getString("BeginningDate")
        val separateDate = arguments?.getString("SeparateDate")
        val maleGender = arguments?.getString("MaleGender")
        val femaleGender = arguments?.getString("FemaleGender")
        pairFlightFemaleKey = arguments?.getString("PairFlightFemaleKey").toString()
        pairFlightMaleKey = arguments?.getString("PairFlightMaleKey").toString()
        pairFemaleKey = arguments?.getString("PairFemaleKey").toString()
        pairMaleKey = arguments?.getString("PairMaleKey").toString()
        pairKey =arguments?.getString("PairKey").toString()
        cagePairKey =arguments?.getString("CagePairKey").toString()

        pairCageBirdFemale = arguments?.getString("CageBirdFemale").toString()
        pairCageBirdMale = arguments?.getString("CageBirdFemale").toString()
        pairCageKeyFemale = arguments?.getString("CageKeyFemale").toString()
        pairCageKeyMale = arguments?.getString("CageKeyMale").toString()
        currentUserId = mAuth.currentUser?.uid.toString()

        fab.setOnClickListener {
            showEggDialog()
        }
        lifecycleScope.launch {
            try {
                val data = getDataFromDatabase()
                dataList.clear()
                dataList.addAll(data)
                adapter.notifyDataSetChanged()
            } catch (e: java.lang.Exception) {
                Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
            }
        }
        refreshApp()
        return view
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
    private suspend fun getDataFromDatabase(): List<EggData> = withContext(Dispatchers.IO) {

        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId}").child("Pairs")
            .child(pairKey).child("Clutches")
        val dataList = ArrayList<EggData>()
        val snapshot = db.get().await()

        for (clutchSnapshot in snapshot.children) {
            val data = clutchSnapshot.getValue(EggData::class.java)!!

            var parentPair = false
            var fosterPair = false

            if (clutchSnapshot.key != "QR" || clutchSnapshot.key != "Parent" || clutchSnapshot.key != "Foster Pair"){

                key = clutchSnapshot.key.toString()
                var incubatingCount = 0
                var laidCount = 0
                var hatchedCount = 0
                var notFertilizedCount = 0
                var brokenCount = 0
                var abandonCount = 0
                var deadInShellCount = 0
                var deadBeforeMovingToNurseryCount = 0
                var moveCount = 0
                var eggsCount = 0

                if (key != "QR"){
                    for (eggSnapshot in clutchSnapshot.children) {

                        val eggStatus = eggSnapshot.child("Status").value.toString()
                        val eggDate = eggSnapshot.child("Date").value.toString()
                        eggsCount++

                        clutchCount = snapshot.childrenCount.toInt()
                        data.clutchCount = clutchCount.toString()
                        data.paircagekey = paircagekey
                        if (eggStatus == "Incubating") {

                            incubatingCount++
                            Log.d(ContentValues.TAG, incubatingCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggIncubating = incubatingCount.toString()
                            data.eggIncubationStartDate = eggDate
                        }
                        if (eggStatus == "Laid") {

                            laidCount++
                            Log.d(ContentValues.TAG, laidCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggLaid = laidCount.toString()
                            data.eggLaidStartDate = eggDate

                        }
                        if (eggStatus == "Hatched") {

                            hatchedCount++
                            Log.d(ContentValues.TAG, laidCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggHatched = hatchedCount.toString()
                            data.eggLaidStartDate = eggDate

                        }
                        if (eggStatus == "Not Fertilized") {

                            notFertilizedCount++
                            Log.d(ContentValues.TAG, notFertilizedCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggNotFertilized = notFertilizedCount.toString()
                            data.eggLaidStartDate = eggDate

                        }
                        if (eggStatus == "Broken") {

                            brokenCount++
                            Log.d(ContentValues.TAG, brokenCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggBroken = brokenCount.toString()
                            data.eggLaidStartDate = eggDate

                        }
                        if (eggStatus == "Abandon") {

                            abandonCount++
                            Log.d(ContentValues.TAG, abandonCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggAbandon = abandonCount.toString()
                            data.eggLaidStartDate = eggDate

                        }
                        if (eggStatus == "Dead in Shell") {

                            deadInShellCount++
                            Log.d(ContentValues.TAG, laidCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggDeadInShell = deadInShellCount.toString()
                            data.eggLaidStartDate = eggDate

                        }
                        if (eggStatus == "Dead Before Moving To Nursery") {

                            deadBeforeMovingToNurseryCount++
                            Log.d(ContentValues.TAG, laidCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggDeadBeforeMovingToNursery = deadBeforeMovingToNurseryCount.toString()
                            data.eggLaidStartDate = eggDate

                        }
                        if (eggStatus == "Moved") {

                            moveCount++
                            Log.d(ContentValues.TAG, laidCount.toString())
                            data.pairKey = pairKey
                            data.eggKey = key
                            data.eggCount = eggsCount.toString()
                            data.eggMoved = moveCount.toString()
                            data.eggLaidStartDate = eggDate

                        }


                        data.pairFlightMaleKey = pairFlightMaleKey
                        data.pairFlightFemaleKey = pairFlightFemaleKey
                        data.pairBirdFemaleKey = pairFemaleKey
                        data.pairBirdMaleKey = pairMaleKey
                        data.pairFemaleId = pairFemale
                        data.pairMaleId = pairMale
                        data.eggcagebirdMale = pairCageBirdMale
                        data.eggcagebirdFemale = pairCageBirdFemale
                        data.eggcagekeyMale = pairCageKeyMale
                        data.eggcagekeyFemale = pairCageKeyFemale
                    }
                }



            }
            if (clutchSnapshot.child("Parent").exists()){
                parentPair = true
            }
            if (clutchSnapshot.child("Foster Pair").exists()){
                fosterPair = true
            }

            data.parentPair = parentPair
            data.fosterPair = fosterPair


            dataList.add(data)
        }

        if(dataList.count()>1){
            totalBirds.text = dataList.count().toString() + " Clutches"
        }
        else{
            totalBirds.text = dataList.count().toString() + " Clutch"
        }
        dataList
    }

    private fun generateQRCodeUri(bundleCageData: String): Uri? {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(bundleCageData, BarcodeFormat.QR_CODE, 400, 400)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)

        // Create a file to store the QR code image
        val storageDir =  requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("QRCode", ".png", storageDir)

        try {
            val stream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // Convert the file URI to a string and return
        return Uri.fromFile(imageFile)
    }

    fun qrAdd(bundle: JSONObject, pushKey: DatabaseReference){


        val imageUri = generateQRCodeUri(bundle.toString())

        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
        val uploadTask = imageUri?.let { it1 -> imageRef.putFile(it1) }

        uploadTask?.addOnSuccessListener { task ->
            imageRef.downloadUrl.addOnSuccessListener{ uri->
                val imageUrl = uri.toString()

                val dataQR: Map<String, Any?> = hashMapOf(
                    "QR" to imageUrl
                )
                pushKey.updateChildren(dataQR)
            }
        }
    }
    private fun showEggDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.showlayout_add_egg, null)
        val numberPicker = dialogLayout.findViewById<NumberPicker>(R.id.numberPicker)
        val checkBox = dialogLayout.findViewById<CheckBox>(R.id.checkbox)

        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Pairs")
            .child(pairKey).child("Clutches")
        val cageRef = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: $currentUserId").child("Cages").child("Breeding Cages").child(paircagekey).child("Pair Birds")
            .child(cagePairKey).child("Clutches")

        checkBox.isChecked = true


        val sharedPrefs = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val maturingValue =
            sharedPrefs?.getString("maturingValue", "50") // Default to 50 if not set
        val maturingDays = maturingValue?.toIntOrNull() ?: 50

        val incubatingValue = sharedPrefs?.getString("incubatingValue", "21")
        val incubatingDays = incubatingValue?.toIntOrNull() ?: 21



        val newClutchRef = db.push()
        val newBreedingClutchRef = cageRef.push()

        numberPicker.minValue = 0
        numberPicker.maxValue = 10

        builder.setTitle("Add Eggs")
        builder.setPositiveButton("Save", null)

        builder.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
        }

        builder.setView(dialogLayout)

        val alertDialog = builder.create()

        alertDialog.setOnShowListener {
            val addButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            var eggValue: Int
            var defaultStatus = "Incubating"

            var currentDate = LocalDate.now()
            var eggCount = 0
            var incubatingCount = 0
            var laidCount = 0
            val formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US)


            val formattedDate = currentDate.format(formatter)
            addButton.setOnClickListener {
                val newEggs = EggData()
                eggValue = numberPicker.value
                if (checkBox.isChecked) {

                    Toast.makeText(
                        requireContext(),
                        "Checked $eggValue",
                        Toast.LENGTH_SHORT
                    ).show()
                    val currentDateTime = LocalDateTime.now()
                    hatchingDateTime = currentDateTime.plusDays(incubatingDays.toLong())
                    for (i in 0 until eggValue) {
                        val clutchesRandomID = kotlin.random.Random.nextInt()
                        val clutches = newClutchRef.push()
                        val newClutches = newBreedingClutchRef.push()
                        clutchkey = clutches.key.toString()
                        eggCount++
                        incubatingCount++
                        val data: Map<String, Any?> = hashMapOf(
                            "Status" to defaultStatus,
                            "Date" to formattedDate,
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays,
                            "Estimated Hatching Date" to hatchingDateTime.format(formatter1),
                            "Alarm ID" to clutchesRandomID
                        )

                        val bundleData = JSONObject()
                        bundleData.put("IncubatingStartDate",incubatingDays)
                        bundleData.put("MaturingStartDate",  maturingDays)
                        bundleData.put("EggKey", key)
                        bundleData.put("IndividualEggKey", clutches.key)
                        bundleData.put("PairKey", pairKey)
                        qrAdd(bundleData, clutches)

                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggIncubating = incubatingCount.toString()
                        newEggs.eggIncubationStartDate = formattedDate
                        newEggs.estimatedHatchedDate = hatchingDateTime.format(formatter1)
                        clutches.updateChildren(data)
                        newClutches.updateChildren(data)
                        Log.d(ContentValues.TAG, "Alarm ID: $clutchesRandomID")
                        setAlarmForEgg(requireContext(), hatchingDateTime.format(formatter1), clutchesRandomID)
                    }
                    dataList.add(newEggs)




                } else if (!checkBox.isChecked) {
                    Toast.makeText(
                        requireContext(),
                        "notChecked $eggValue",
                        Toast.LENGTH_SHORT
                    ).show()

                    defaultStatus = "Laid"
                    for (i in 0 until eggValue) {
                        val clutches = newClutchRef.push()
                        val newClutches = newBreedingClutchRef.push()
                        eggCount++
                        laidCount++
                        val data: Map<String, Any?> = hashMapOf(
                            "Status" to defaultStatus,
                            "Date" to formattedDate,
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays
                        )
                        val bundleData = JSONObject()
                        bundleData.put("IncubatingStartDate",incubatingDays)
                        bundleData.put("MaturingStartDate",  maturingDays)
                        bundleData.put("EggKey", key)
                        bundleData.put("IndividualEggKey", clutches.key)
                        bundleData.put("PairKey", pairKey)
                        qrAdd(bundleData, clutches)

                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggLaid = laidCount.toString()
                        newEggs.eggLaidStartDate = formattedDate
                        newClutches.updateChildren(data)

                        clutches.updateChildren(data)

                    }
                    dataList.add(newEggs)

                }
                lifecycleScope.launch {
                    try {
                        val data = getDataFromDatabase()
                        dataList.clear()
                        dataList.addAll(data)
                        adapter.notifyDataSetChanged()
                    } catch (e: java.lang.Exception) {
                        Log.e(ContentValues.TAG, "Error retrieving data: ${e.message}")
                    }
                }
                val bundleData = JSONObject()
                bundleData.put("ClutchQR",true)
                bundleData.put("PairKey",pairKey)
                bundleData.put("ClutchKey",  newClutchRef.key)
                bundleData.put("EggKey", newClutchRef.key)
                bundleData.put("PairFlightMaleKey", pairFlightMaleKey)
                bundleData.put("PairFlightFemaleKey", pairFlightFemaleKey)
                bundleData.put("PairMaleKey", pairMaleKey)
                bundleData.put("PairFemaleKey", pairFemaleKey)
                bundleData.put("PairMaleID", pairMale)
                bundleData.put("PairFemaleID", pairFemale)
                bundleData.put("CageKeyFemale", pairCageKeyFemale)
                bundleData.put("CageKeyMale", pairCageKeyMale)
                bundleData.put("CageBirdFemale",pairCageBirdFemale)
                bundleData.put("CageBirdMale",pairCageBirdMale)
                qrAdd(bundleData, newClutchRef)
                alertDialog.dismiss()
            }

        }

        alertDialog.show()

    }

    fun setAlarmForEgg(context: Context, estimatedHatchDate: String, eggIndex: Int) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MyAlarmReceiver::class.java)
        intent.putExtra("egg_index", eggIndex) // Pass the index of the egg
        intent.putExtra("pairmale", pairMale)
        intent.putExtra("pairfemale", pairFemale)
        intent.putExtra("clutchkey", clutchkey)
        intent.putExtra("pairkey", pairKey)
        intent.putExtra("Eggkey", key)
        intent.putExtra("pairflightfemalekey", pairFlightFemaleKey)
        intent.putExtra("pairflightmalekey", pairFlightMaleKey)
        intent.putExtra("pairmalekey", pairMaleKey)
        intent.putExtra("pairfemalekey", pairFemaleKey)
        intent.putExtra("cagekeyfemale", pairCageKeyFemale)
        intent.putExtra("cagekeymale", pairCageKeyMale)
        intent.putExtra("cagebirdfemale", pairCageBirdFemale)
        intent.putExtra("cagebirdmale", pairCageBirdMale)
        intent.putExtra("estimatedHatchDate", hatchingDateTime.format(formatter1))
        val pendingIntent = PendingIntent.getBroadcast(context, eggIndex, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val hatchDateTime = LocalDateTime.parse(estimatedHatchDate, formatter1)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = hatchDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

    }

//    fun setWorkForEgg(context: Context, estimatedHatchDate: String, eggIndex: Int) {
//        val hatchDateTime = LocalDateTime.parse(estimatedHatchDate, formatter1)
//        val epochMillis = hatchDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
//
//        val inputData = Data.Builder()
//            .putInt("egg_index", eggIndex)
//            .build()
//
//        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
//            .setInitialDelay(epochMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
//            .setInputData(inputData)
//            .build()
//
//        WorkManager.getInstance(context).enqueue(workRequest)
//    }
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ClutchesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClutchesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}