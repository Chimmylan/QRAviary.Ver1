package com.example.qraviaryapp.fragments.Pairs

import EggData
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.PreviousClutchesListAdapter
import com.google.android.material.button.MaterialButton
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
 * Use the [ClutchesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreviousClutchesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tvDate: TextView
    private lateinit var tvMutations: TextView
    private lateinit var btnMale: MaterialButton
    private lateinit var btnFemale: MaterialButton

    private lateinit var mAuth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PreviousClutchesListAdapter
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
    private lateinit var swipeToRefresh: SwipeRefreshLayout
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
        val view = inflater.inflate(R.layout.fragment_previous_clutches, container, false)

        mAuth = FirebaseAuth.getInstance()
        dataList = ArrayList()
        val gridLayoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = gridLayoutManager
        adapter = PreviousClutchesListAdapter(requireContext(), dataList)
        recyclerView.adapter = adapter
        swipeToRefresh = view.findViewById(R.id.swipeToRefresh)

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