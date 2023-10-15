package com.example.qraviaryapp.fragments.NavFragments

import BirdBarChart
import BirdData
import BirdGenderBarChart
import BirdStatusBarChart
import EggData
import PairBarChart
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StatisticsFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    lateinit var birdList: ArrayList<BirdBarChart>
    lateinit var birdList1: ArrayList<PairBarChart>
    private lateinit var switch: SwitchCompat
    private var isSwitchOn = false
    private lateinit var rball: RadioButton
    private lateinit var rbsplit: RadioButton
    private lateinit var rbnotsplit: RadioButton

    private var allBirds = false
    private var isSplit = false
    private var notSplit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)
        switch = view.findViewById(R.id.switchbtn)
        rball = view.findViewById(R.id.radio_all_birds)
        rbnotsplit = view.findViewById(R.id.radio_not_split)
        rbsplit = view.findViewById(R.id.radio_split)
        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (!isNetworkAvailable) {
                    showSnackbar("Your Internet connection was restored")
                }
                isNetworkAvailable = true
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                showSnackbar("You are currently offline")
                isNetworkAvailable = false
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            isSwitchOn = isChecked
            updateBarChart()
            if (isSwitchOn) {
                // Reload the charts with filtered data
                setupBarChart(view.findViewById(R.id.birdsChart), birdList)
                setupStatusBarChart(view.findViewById(R.id.birdstatusChart), birdList)
            } else {
                // Load the charts with the original data
                setupBarChart(view.findViewById(R.id.birdsChart), birdList)

                setupStatusBarChart(view.findViewById(R.id.birdstatusChart), birdList)
            }
        }
        rball.isChecked = true
        allBirds = true
        rball.setOnClickListener {
            updateBarChart()
        }

        rbsplit.setOnClickListener {
            updateBarChart()
        }

        rbnotsplit.setOnClickListener {
            updateBarChart()
        }
        mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Birds")
        val databaseReference1 =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Pairs")


        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                birdList = ArrayList()
                for (itemSnapshot in dataSnapshot.children) {
                    val birdsSnapshot = itemSnapshot.child("Birds").value
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    if (data != null) {
                        val gender = itemSnapshot.child("Gender").value
                        val status = itemSnapshot.child("Status").value
                        val mutation1 = itemSnapshot.child("Mutation1").child("Mutation Name").value
                        val mutation2 = itemSnapshot.child("Mutation2").child("Mutation Name").value
                        val mutation3 = itemSnapshot.child("Mutation3").child("Mutation Name").value
                        val mutation4 = itemSnapshot.child("Mutation4").child("Mutation Name").value
                        val mutation5 = itemSnapshot.child("Mutation5").child("Mutation Name").value
                        val mutation6 = itemSnapshot.child("Mutation6").child("Mutation Name").value

                        val nonNullMutations = listOf(
                            mutation1,
                            mutation2,
                            mutation3,
                            mutation4,
                            mutation5,
                            mutation6
                        ).filter { it != null }
                        val NonNullMutations = mutableListOf<String>()
                        for (mutation in nonNullMutations) {
                            if (mutation != "null") {
                                NonNullMutations.add(mutation.toString())
                            }
                        }
                        val CombinedMutations = if (NonNullMutations.isNotEmpty()) {
                            NonNullMutations.joinToString(" x ")

                        } else {
                            "Mutation: None"
                        }

                        val sex = gender.toString()
                        val birdstatus = status.toString()
                        val birdBarChart= BirdBarChart(CombinedMutations,sex,birdstatus)
                        birdList.add(birdBarChart)
                    }
                }

                val birdStatusChart = view.findViewById<HorizontalBarChart>(R.id.birdstatusChart)
                setupStatusBarChart(birdStatusChart, birdList)
                val birdChart = view.findViewById<HorizontalBarChart>(R.id.birdsChart)
                setupBarChart(birdChart, birdList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        databaseReference1.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                birdList1 = ArrayList()
                for (itemSnapshot in dataSnapshot.children) {
                    val femaleBird = itemSnapshot.child("Female Mutation").value
                    val maleBird = itemSnapshot.child("Male Mutation").value
                    val clutchesSnapshot = itemSnapshot.child("Clutches")
                    val femalebird = femaleBird.toString()
                    val malebird = maleBird.toString()
                    for (clutchSnapshot in clutchesSnapshot.children) {
                        for (eggSnapshot in clutchSnapshot.children) {
                            val clutchData = eggSnapshot.getValue(EggData::class.java)
                            if (clutchData != null) {
                                val status = eggSnapshot.child("Status").value

                                val eggstatus = status.toString()



                                val pairBarChart = PairBarChart(malebird, femalebird, eggstatus)

                                birdList1.add(pairBarChart)
                            }
                        }
                    }

                }
                val pairChart = view.findViewById<HorizontalBarChart>(R.id.eggbarChart)
                setupPairBarChart(pairChart, birdList1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return view
    }
    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }
    fun updateBarChart() {

        if (rball.isChecked) {
            // Show all birds with mutations
            allBirds = true
            isSplit = false
            notSplit = false
            Log.d(ContentValues.TAG, allBirds.toString())
            setupBarChart(requireView().findViewById(R.id.birdsChart), birdList)
            setupStatusBarChart(requireView().findViewById(R.id.birdstatusChart), birdList)
            setupPairBarChart(requireView().findViewById(R.id.eggbarChart), birdList1)
        } else if (rbsplit.isChecked) {
            allBirds = false
            isSplit = true
            notSplit = false
            Log.d(ContentValues.TAG, isSplit.toString())
            // Show birds with only one mutation (split)
            val splitBirdList = birdList.filter { it.mutations?.contains("x") != true }
            setupBarChart(requireView().findViewById(R.id.birdsChart), splitBirdList)
            setupStatusBarChart(requireView().findViewById(R.id.birdstatusChart), birdList)
            setupPairBarChart(requireView().findViewById(R.id.eggbarChart), birdList1)

        } else if (rbnotsplit.isChecked) {
            allBirds = false
            isSplit = false
            notSplit = true

            // Show birds with combined mutations (not split)
            val notSplitBirdList = birdList.filter { it.mutations?.contains("x") == true }
            setupBarChart(requireView().findViewById(R.id.birdsChart), notSplitBirdList)
            setupStatusBarChart(requireView().findViewById(R.id.birdstatusChart), notSplitBirdList)
            setupPairBarChart(requireView().findViewById(R.id.eggbarChart),birdList1)

        }
    }
    private fun setupPairBarChart(barChart: HorizontalBarChart, birdList1: List<PairBarChart>) {
        val MutationCounts = mutableMapOf<String, MutableMap<String, Int>>()

        for (pairBarChart in birdList1) {
            val mutation1 = pairBarChart.malemutations ?: "Mutation: None"
            val mutation2 = pairBarChart.femalemutations ?: "Mutation: None"
            val status = pairBarChart.status ?: "Dead Before Moving To Nursery"

            val mutationKey1 = mutation1

            // If the mutation key is not in the outer map, add it
            if (allBirds) {
                if (!MutationCounts.containsKey(mutationKey1)) {
                    MutationCounts[mutationKey1] = mutableMapOf(
                        "Incubating" to 0,
                        "Hatched" to 0,
                        "Not Fertilized" to 0,
                        "Broken" to 0,
                        "Abandon" to 0,
                        "Dead in Shell" to 0,
                        "Dead Before Moving To Nursery" to 0,
                        "Moved" to 0,
                        "Laid" to 0
                    )
                }
                // Use mutationKey1 to access the inner map
                var mutationCounts: MutableMap<String, Int>? = MutationCounts[mutationKey1]

                when (status) {
                    "Incubating" -> mutationCounts?.set("Incubating", mutationCounts["Incubating"]!! + 1)
                    "Hatched" -> mutationCounts?.set("Hatched", mutationCounts["Hatched"]!! + 1)
                    "Not Fertilized" -> mutationCounts?.set("Not Fertilized", mutationCounts["Not Fertilized"]!! + 1)
                    "Broken" -> mutationCounts?.set("Broken", mutationCounts["Broken"]!! + 1)
                    "Abandon" -> mutationCounts?.set("Abandon", mutationCounts["Abandon"]!! + 1)
                    "Dead in Shell" -> mutationCounts?.set("Dead in Shell", mutationCounts["Dead in Shell"]!! + 1)
                    "Moved" -> mutationCounts?.set("Moved", mutationCounts["Moved"]!! + 1)
                    "Laid" -> mutationCounts?.set("Laid", mutationCounts["Laid"]!! + 1)
                    else -> mutationCounts?.set("Dead Before Moving To Nursery", mutationCounts["Dead Before Moving To Nursery"]!! + 1)
                }
                if (mutation1 != mutation2) {
                    val mutationKey2 = mutation2
                    if (!MutationCounts.containsKey(mutationKey2)) {
                        MutationCounts[mutationKey2] = mutableMapOf(
                            "Incubating" to 0,
                            "Hatched" to 0,
                            "Not Fertilized" to 0,
                            "Broken" to 0,
                            "Abandon" to 0,
                            "Dead in Shell" to 0,
                            "Dead Before Moving To Nursery" to 0,
                            "Moved" to 0,
                            "Laid" to 0
                        )
                    }
                    var mutationCounts2: MutableMap<String, Int>? = MutationCounts[mutationKey2]

                    when (status) {
                        "Incubating" -> mutationCounts2?.set("Incubating", mutationCounts2["Incubating"]!! + 1)
                        "Hatched" -> mutationCounts2?.set("Hatched", mutationCounts2["Hatched"]!! + 1)
                        "Not Fertilized" -> mutationCounts2?.set("Not Fertilized", mutationCounts2["Not Fertilized"]!! + 1)
                        "Broken" -> mutationCounts2?.set("Broken", mutationCounts2["Broken"]!! + 1)
                        "Abandon" -> mutationCounts2?.set("Abandon", mutationCounts2["Abandon"]!! + 1)
                        "Dead in Shell" -> mutationCounts2?.set("Dead in Shell", mutationCounts2["Dead in Shell"]!! + 1)
                        "Moved" -> mutationCounts2?.set("Moved", mutationCounts2["Moved"]!! + 1)
                        "Laid" -> mutationCounts2?.set("Laid", mutationCounts2["Laid"]!! + 1)
                        else -> mutationCounts2?.set("Dead Before Moving To Nursery", mutationCounts2["Dead Before Moving To Nursery"]!! + 1)
                    }
                }
            } else if (isSplit) {
                if (!MutationCounts.containsKey(mutationKey1) && mutation1.contains("x")) {
                    MutationCounts[mutationKey1] = mutableMapOf(
                        "Incubating" to 0,
                        "Hatched" to 0,
                        "Not Fertilized" to 0,
                        "Broken" to 0,
                        "Abandon" to 0,
                        "Dead in Shell" to 0,
                        "Dead Before Moving To Nursery" to 0,
                        "Moved" to 0,
                        "Laid" to 0
                    )
                }
                // Use mutationKey1 to access the inner map
                var mutationCounts: MutableMap<String, Int>? = MutationCounts[mutationKey1]

                when (status) {
                    "Incubating" -> mutationCounts?.set("Incubating", mutationCounts["Incubating"]!! + 1)
                    "Hatched" -> mutationCounts?.set("Hatched", mutationCounts["Hatched"]!! + 1)
                    "Not Fertilized" -> mutationCounts?.set("Not Fertilized", mutationCounts["Not Fertilized"]!! + 1)
                    "Broken" -> mutationCounts?.set("Broken", mutationCounts["Broken"]!! + 1)
                    "Abandon" -> mutationCounts?.set("Abandon", mutationCounts["Abandon"]!! + 1)
                    "Dead in Shell" -> mutationCounts?.set("Dead in Shell", mutationCounts["Dead in Shell"]!! + 1)
                    "Moved" -> mutationCounts?.set("Moved", mutationCounts["Moved"]!! + 1)
                    "Laid" -> mutationCounts?.set("Laid", mutationCounts["Laid"]!! + 1)
                    else -> mutationCounts?.set("Dead Before Moving To Nursery", mutationCounts["Dead Before Moving To Nursery"]!! + 1)
                }
                if (mutation1 != mutation2) {
                    val mutationKey2 = mutation2
                    if (!MutationCounts.containsKey(mutationKey2) && mutation2.contains("x")) {
                        MutationCounts[mutationKey2] = mutableMapOf(
                            "Incubating" to 0,
                            "Hatched" to 0,
                            "Not Fertilized" to 0,
                            "Broken" to 0,
                            "Abandon" to 0,
                            "Dead in Shell" to 0,
                            "Dead Before Moving To Nursery" to 0,
                            "Moved" to 0,
                            "Laid" to 0
                        )
                    }
                    var mutationCounts2: MutableMap<String, Int>? = MutationCounts[mutationKey2]

                    when (status) {
                        "Incubating" -> mutationCounts2?.set("Incubating", mutationCounts2["Incubating"]!! + 1)
                        "Hatched" -> mutationCounts2?.set("Hatched", mutationCounts2["Hatched"]!! + 1)
                        "Not Fertilized" -> mutationCounts2?.set("Not Fertilized", mutationCounts2["Not Fertilized"]!! + 1)
                        "Broken" -> mutationCounts2?.set("Broken", mutationCounts2["Broken"]!! + 1)
                        "Abandon" -> mutationCounts2?.set("Abandon", mutationCounts2["Abandon"]!! + 1)
                        "Dead in Shell" -> mutationCounts2?.set("Dead in Shell", mutationCounts2["Dead in Shell"]!! + 1)
                        "Moved" -> mutationCounts2?.set("Moved", mutationCounts2["Moved"]!! + 1)
                        "Laid" -> mutationCounts2?.set("Laid", mutationCounts2["Laid"]!! + 1)
                        else -> mutationCounts2?.set("Dead Before Moving To Nursery", mutationCounts2["Dead Before Moving To Nursery"]!! + 1)
                    }
                }
            } else if (notSplit) {
                if (!MutationCounts.containsKey(mutationKey1) && !mutation1.contains("x")) {
                    MutationCounts[mutationKey1] = mutableMapOf(
                        "Incubating" to 0,
                        "Hatched" to 0,
                        "Not Fertilized" to 0,
                        "Broken" to 0,
                        "Abandon" to 0,
                        "Dead in Shell" to 0,
                        "Dead Before Moving To Nursery" to 0,
                        "Moved" to 0,
                        "Laid" to 0
                    )
                }
                // Use mutationKey1 to access the inner map
                var mutationCounts: MutableMap<String, Int>? = MutationCounts[mutationKey1]

                when (status) {
                    "Incubating" -> mutationCounts?.set("Incubating", mutationCounts["Incubating"]!! + 1)
                    "Hatched" -> mutationCounts?.set("Hatched", mutationCounts["Hatched"]!! + 1)
                    "Not Fertilized" -> mutationCounts?.set("Not Fertilized", mutationCounts["Not Fertilized"]!! + 1)
                    "Broken" -> mutationCounts?.set("Broken", mutationCounts["Broken"]!! + 1)
                    "Abandon" -> mutationCounts?.set("Abandon", mutationCounts["Abandon"]!! + 1)
                    "Dead in Shell" -> mutationCounts?.set("Dead in Shell", mutationCounts["Dead in Shell"]!! + 1)
                    "Moved" -> mutationCounts?.set("Moved", mutationCounts["Moved"]!! + 1)
                    "Laid" -> mutationCounts?.set("Laid", mutationCounts["Laid"]!! + 1)
                    else -> mutationCounts?.set("Dead Before Moving To Nursery", mutationCounts["Dead Before Moving To Nursery"]!! + 1)
                }
                if (mutation1 != mutation2) {
                    val mutationKey2 = mutation2
                    if (!MutationCounts.containsKey(mutationKey2) && !mutation2.contains("x")) {
                        MutationCounts[mutationKey2] = mutableMapOf(
                            "Incubating" to 0,
                            "Hatched" to 0,
                            "Not Fertilized" to 0,
                            "Broken" to 0,
                            "Abandon" to 0,
                            "Dead in Shell" to 0,
                            "Dead Before Moving To Nursery" to 0,
                            "Moved" to 0,
                            "Laid" to 0
                        )
                    }
                    var mutationCounts2: MutableMap<String, Int>? = MutationCounts[mutationKey2]

                    when (status) {
                        "Incubating" -> mutationCounts2?.set("Incubating", mutationCounts2["Incubating"]!! + 1)
                        "Hatched" -> mutationCounts2?.set("Hatched", mutationCounts2["Hatched"]!! + 1)
                        "Not Fertilized" -> mutationCounts2?.set("Not Fertilized", mutationCounts2["Not Fertilized"]!! + 1)
                        "Broken" -> mutationCounts2?.set("Broken", mutationCounts2["Broken"]!! + 1)
                        "Abandon" -> mutationCounts2?.set("Abandon", mutationCounts2["Abandon"]!! + 1)
                        "Dead in Shell" -> mutationCounts2?.set("Dead in Shell", mutationCounts2["Dead in Shell"]!! + 1)
                        "Moved" -> mutationCounts2?.set("Moved", mutationCounts2["Moved"]!! + 1)
                        "Laid" -> mutationCounts2?.set("Laid", mutationCounts2["Laid"]!! + 1)
                        else -> mutationCounts2?.set("Dead Before Moving To Nursery", mutationCounts2["Dead Before Moving To Nursery"]!! + 1)
                    }
                }
            }

            val barEntries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()

            MutationCounts.entries.forEachIndexed { index, entry ->
                val mutationCombination = entry.key
                val counts = entry.value
                val barEntry = BarEntry(
                    index.toFloat(), floatArrayOf(
                        counts["Incubating"]!!.toFloat(),
                        counts["Hatched"]!!.toFloat(),
                        counts["Not Fertilized"]!!.toFloat(),
                        counts["Broken"]!!.toFloat(),
                        counts["Abandon"]!!.toFloat(),
                        counts["Dead in Shell"]!!.toFloat(),
                        counts["Moved"]!!.toFloat(),
                        counts["Laid"]!!.toFloat(),
                        counts["Dead Before Moving To Nursery"]!!.toFloat()
                    )
                )
                barEntries.add(barEntry)
                labels.add(mutationCombination)
            }

            val legend = barChart.legend
            legend.form = Legend.LegendForm.CIRCLE
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.isWordWrapEnabled = true
            val dataSet = BarDataSet(barEntries, "")

            val labels1 = listOf(
                "Incubating", "Hatched", "Not Fertilized", "Broken", "Abandon",
                "Dead in Shell", "Moved", "Laid", "Dead Before Moving To Nursery"
            )
            dataSet.stackLabels = labels1.toTypedArray()
            dataSet.colors = listOf(
                Color.GREEN,
                Color.BLUE,
                Color.RED,
                Color.BLACK,
                Color.CYAN,
                Color.MAGENTA,
                Color.rgb(255, 182, 193),
                Color.YELLOW,
                Color.GRAY
            )

            val data = BarData(dataSet)
            barChart.data = data
            barChart.description.isEnabled = false
            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(true)
            barChart.setPinchZoom(false)
            barChart.isDoubleTapToZoomEnabled = false
            barChart.animateXY(1000, 1000)

            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = barEntries.size

            xAxis.valueFormatter = IndexAxisValueFormatter(labels)

            barChart.invalidate()
        }
    }

    private fun setupBarChart(barChart: HorizontalBarChart, birdList: List<BirdBarChart>) {
        val filteredBirdList = if (isSwitchOn) {
            birdList.filter { it.status in listOf("Available", "For Sale", "Paired") }
        } else {
            birdList
        }

        val combinedMutationCounts = mutableMapOf<String, MutableMap<String, Int>>()

        for (birdBarChart in filteredBirdList) {
            val combinedMutations = birdBarChart.mutations ?: "Mutation: None"
            val sex = birdBarChart.gender ?: "Unknown"

            if (combinedMutations != "Mutation: None") {
                if (!combinedMutationCounts.containsKey(combinedMutations)) {
                    combinedMutationCounts[combinedMutations] =
                        mutableMapOf("Male" to 0, "Female" to 0, "Unknown" to 0)
                }

                val mutationCounts = combinedMutationCounts[combinedMutations]!!

                when (sex) {
                    "Male" -> mutationCounts["Male"] = mutationCounts["Male"]!! + 1
                    "Female" -> mutationCounts["Female"] = mutationCounts["Female"]!! + 1
                    else -> mutationCounts["Unknown"] = mutationCounts["Unknown"]!! + 1
                }
            }

            val legend = barChart.legend
            legend.form = Legend.LegendForm.CIRCLE
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.isWordWrapEnabled = true

            val barEntries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()

            combinedMutationCounts.entries.forEachIndexed { index, entry ->
                val mutationCombination = entry.key
                val counts = entry.value
                val barEntry = BarEntry(
                    index.toFloat(),
                    floatArrayOf(
                        counts["Male"]!!.toFloat(),
                        counts["Female"]!!.toFloat(),
                        counts["Unknown"]!!.toFloat()
                    )
                )
                barEntries.add(barEntry)
                labels.add(mutationCombination)
            }
            val dataSet = BarDataSet(barEntries, "")

            val labels1 = listOf("Male", "Female", "Unknown")
            dataSet.stackLabels = labels1.toTypedArray()
            dataSet.colors = listOf(Color.BLUE, Color.rgb(255, 182, 193), Color.BLACK)

            val data = BarData(dataSet)
            barChart.data = data
            barChart.description.isEnabled = false
            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(true)
            barChart.setPinchZoom(false)
            barChart.isDoubleTapToZoomEnabled = false
            barChart.animateXY(1000, 1000)

            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = barEntries.size
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)


            barChart.invalidate()
        }
    }
    class CombinedMutationXAxisValueFormatter(private val combinedMutationCounts: Map<String, MutableMap<String, Int>>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            val mutationCombination = combinedMutationCounts.keys.elementAtOrNull(index)
            return mutationCombination ?: ""
        }
    }
    private fun setupStatusBarChart(barChart: HorizontalBarChart, birdList: List<BirdBarChart>) {
        val filteredBirdList = if (isSwitchOn) {
            birdList.filter { it.status in listOf("Available", "For Sale", "Paired") }
        } else {
            birdList
        }

        val combinedMutationCounts = mutableMapOf<String, MutableMap<String, Int>>()

        for (birdBarChart in filteredBirdList) {
            val combinedMutations = birdBarChart.mutations ?: "Mutation: None"
            val status = birdBarChart.status ?: "Paired"

            if (combinedMutations != "Mutation: None") {
                if (!combinedMutationCounts.containsKey(combinedMutations)) {
                    combinedMutationCounts[combinedMutations] = mutableMapOf(
                        "Available" to 0, "For Sale" to 0, "Sold" to 0, "Deceased" to 0,
                        "Exchanged" to 0, "Lost" to 0, "Donated" to 0, "Other" to 0, "Paired" to 0
                    )
                }

                val mutationCounts = combinedMutationCounts[combinedMutations]!!

                when (status) {
                    "Available" -> mutationCounts["Available"] = mutationCounts["Available"]!! + 1
                    "For Sale" -> mutationCounts["For Sale"] = mutationCounts["For Sale"]!! + 1
                    "Sold" -> mutationCounts["Sold"] = mutationCounts["Sold"]!! + 1
                    "Deceased" -> mutationCounts["Deceased"] = mutationCounts["Deceased"]!! + 1
                    "Exchanged" -> mutationCounts["Exchanged"] = mutationCounts["Exchanged"]!! + 1
                    "Lost" -> mutationCounts["Lost"] = mutationCounts["Lost"]!! + 1
                    "Donated" -> mutationCounts["Donated"] = mutationCounts["Donated"]!! + 1
                    "Other" -> mutationCounts["Other"] = mutationCounts["Other"]!! + 1
                    else -> mutationCounts["Paired"] = mutationCounts["Paired"]!! + 1
                }
            }


            val barEntries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()

            combinedMutationCounts.entries.forEachIndexed { index, entry ->
                val mutationCombination = entry.key
                val counts = entry.value
                val barEntry = BarEntry(
                    index.toFloat(),
                    floatArrayOf(
                        counts["Available"]!!.toFloat(), counts["For Sale"]!!.toFloat(),
                        counts["Sold"]!!.toFloat(), counts["Deceased"]!!.toFloat(),
                        counts["Exchanged"]!!.toFloat(), counts["Lost"]!!.toFloat(),
                        counts["Donated"]!!.toFloat(), counts["Other"]!!.toFloat(),
                        counts["Paired"]!!.toFloat()
                    )
                )
                barEntries.add(barEntry)
                labels.add(mutationCombination)
            }

            val legend = barChart.legend
            legend.form = Legend.LegendForm.CIRCLE
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.isWordWrapEnabled = true

            val dataSet = BarDataSet(barEntries, "")

            val labels1 = listOf(
                "Available",
                "For Sale",
                "Sold",
                "Deceased",
                "Exchanged",
                "Lost",
                "Donated",
                "Other",
                "Paired"
            )

            dataSet.stackLabels = labels1.toTypedArray()
            dataSet.colors = listOf(
                Color.GREEN,
                Color.BLUE,
                Color.RED,
                Color.BLACK,
                Color.CYAN,
                Color.MAGENTA,
                Color.YELLOW,
                Color.LTGRAY,
                Color.rgb(255, 182, 193)
            )

            val data = BarData(dataSet)
            barChart.data = data
            barChart.description.isEnabled = false
            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(true)
            barChart.setPinchZoom(false)
            barChart.isDoubleTapToZoomEnabled = false
            barChart.animateXY(1000, 1000)

            val xAxis = barChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.labelCount = barEntries.size

            xAxis.valueFormatter = IndexAxisValueFormatter(labels)

            barChart.invalidate()
        }
    }
    class BarValueFormatter1(private val combinedMutationCounts: Map<String, MutableMap<String, Int>>) : ValueFormatter() {
        override fun getBarLabel(barEntry: BarEntry?): String {
            val index = barEntry?.x?.toInt() ?: 0
            val mutationCombination = barEntry?.x?.toInt()?.let { combinedMutationCounts.keys.elementAt(it) }
            val counts = mutationCombination?.let { combinedMutationCounts[it] }
            val available = counts?.get("Available") ?: 0
            val forsale = counts?.get("For Sale") ?: 0
            val sold = counts?.get("Sold") ?: 0
            val deceased = counts?.get("Available") ?: 0
            val lost = counts?.get("For Sale") ?: 0
            val exchanged = counts?.get("Sold") ?: 0
            val other = counts?.get("Available") ?: 0
            val donated = counts?.get("For Sale") ?: 0
            val paired = counts?.get("Sold") ?: 0
            return "Available: $available, For Sale: $forsale, Sold: $sold, Deceased: $deceased, " +
                    "Lost: $lost, Exchanged: $exchanged, Donated: $donated, Other: $other, Paired: $paired"
        }
    }



}