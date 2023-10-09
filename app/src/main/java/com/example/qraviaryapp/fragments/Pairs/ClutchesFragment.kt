package com.example.qraviaryapp.fragments.Pairs

import EggData
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.ClutchesListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
    private lateinit var currentUserId: String
    private lateinit var totalclutch: TextView
    private var clutchCount = 0

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

        mAuth = FirebaseAuth.getInstance()
        dataList = ArrayList()
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = gridLayoutManager
        adapter = ClutchesListAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter


        fab = view.findViewById(R.id.fab)
        pairKey = arguments?.getString("BirdKey").toString()
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
        return view
    }
    private suspend fun getDataFromDatabase(): List<EggData> = withContext(Dispatchers.IO) {

        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId}").child("Pairs")
            .child(pairKey).child("Clutches")
        val dataList = ArrayList<EggData>()
        val snapshot = db.get().await()
        for (clutchSnapshot in snapshot.children) {
            val data = clutchSnapshot.getValue(EggData::class.java)
            val key = clutchSnapshot.key.toString()
            var incubatingCount = 0
            var laidCount = 0
            var hatchedCount = 0
            var notFertilizedCount = 0
            var brokenCount = 0
            var abandonCount = 0
            var deadInShellCount = 0
            var deadBeforeMovingToNurseryCount = 0
            var eggsCount = 0

            if (data != null) {
                for (eggSnapshot in clutchSnapshot.children) {
                    val eggData = eggSnapshot.getValue(EggData::class.java)

                    val eggStatus = eggSnapshot.child("Status").value.toString()
                    val eggDate = eggSnapshot.child("Date").value.toString()
                    eggsCount++

                    clutchCount = snapshot.childrenCount.toInt()
                    data.clutchCount = clutchCount.toString()
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

            if (data != null) {
                dataList.add(data)
            }

        }
//        totalclutch.text = "Total Clutch: $clutchCount"
        dataList
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

        checkBox.isChecked = true


        val sharedPrefs = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val maturingValue =
            sharedPrefs?.getString("maturingValue", "50") // Default to 50 if not set
        val maturingDays = maturingValue?.toIntOrNull() ?: 50

        val incubatingValue = sharedPrefs?.getString("incubatingValue", "21")
        val incubatingDays = incubatingValue?.toIntOrNull() ?: 21



        val newClutchRef = db.push()

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

                    for (i in 0 until eggValue) {
                        val clutches = newClutchRef.push()
                        eggCount++
                        incubatingCount++
                        val data: Map<String, Any?> = hashMapOf(
                            "Status" to defaultStatus,
                            "Date" to formattedDate,
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays
                        )

                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggIncubating = incubatingCount.toString()
                        newEggs.eggIncubationStartDate = formattedDate

                        clutches.updateChildren(data)

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
                        eggCount++
                        laidCount++
                        val data: Map<String, Any?> = hashMapOf(
                            "Status" to defaultStatus,
                            "Date" to formattedDate,
                            "Incubating Days" to incubatingDays,
                            "Maturing Days" to maturingDays
                        )

                        newEggs.eggCount = eggCount.toString()
                        newEggs.eggLaid = laidCount.toString()
                        newEggs.eggLaidStartDate = formattedDate

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

                alertDialog.dismiss()
            }

        }

        alertDialog.show()

    }
    override fun onResume() {
        super.onResume()
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
    }
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