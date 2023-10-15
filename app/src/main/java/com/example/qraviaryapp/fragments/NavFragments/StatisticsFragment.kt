package com.example.qraviaryapp.fragments.NavFragments

import BirdBarChart
import BirdData
import BirdGenderBarChart
import BirdStatusBarChart
import EggData
import PairBarChart
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.FragmentAdapter
import com.github.mikephil.charting.charts.BarChart
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
    private lateinit var viewPager: ViewPager
    private lateinit var tablayout: TabLayout
    private lateinit var fragmentAdapter: FragmentAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    lateinit var genderList: ArrayList<BirdGenderBarChart>
    lateinit var statusList: ArrayList<BirdStatusBarChart>
    lateinit var birdList: ArrayList<BirdBarChart>
    lateinit var birdList1: ArrayList<PairBarChart>
    private var sexcount: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

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

        mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Birds")
        val databaseReference1 =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Pairs")


        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                genderList = ArrayList()
                statusList = ArrayList()
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
                        val birdGenderBarChart = BirdGenderBarChart(sex)
                        val birdStatusBarChart = BirdStatusBarChart(birdstatus)
                        val birdBarChart= BirdBarChart(CombinedMutations,sex,birdstatus)
                        genderList.add(birdGenderBarChart)
                        statusList.add(birdStatusBarChart)
                        birdList.add(birdBarChart)
                    }
                }

                val birdGenderChart = view.findViewById<BarChart>(R.id.birdsgenderChart)
                setupGenderBarChart(birdGenderChart, genderList)
                val birdStatusChart = view.findViewById<BarChart>(R.id.birdstatusChart)
                setupStatusBarChart(birdStatusChart, birdList)
                val birdChart = view.findViewById<BarChart>(R.id.birdsChart)
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
                val pairChart = view.findViewById<BarChart>(R.id.eggbarChart)
                setupPairBarChart(pairChart, birdList1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        return view
    }
    private fun setupPairBarChart(barChart: BarChart, birdList1: List<PairBarChart>) {
        val MutationCounts = mutableMapOf<String, MutableMap<String, Int>>()

        for (pairBarChart in birdList1) {
            val mutation1 = pairBarChart.malemutations ?: "Mutation: None"
            val mutation2 = pairBarChart.femalemutations ?: "Mutation: None"
            val status = pairBarChart.status ?: "Dead Before Moving To Nursery"

            val mutationKey1 = mutation1

            // If the mutation key is not in the outer map, add it
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
            val mutationCounts = MutationCounts[mutationKey1]!!
            when (status) {
                "Incubating" -> mutationCounts["Incubating"] = mutationCounts["Incubating"]!! + 1
                "Hatched" -> mutationCounts["Hatched"] = mutationCounts["Hatched"]!! + 1
                "Not Fertilized" -> mutationCounts["Not Fertilized"] =
                    mutationCounts["Not Fertilized"]!! + 1

                "Broken" -> mutationCounts["Broken"] = mutationCounts["Broken"]!! + 1
                "Abandon" -> mutationCounts["Abandon"] = mutationCounts["Abandon"]!! + 1
                "Dead in Shell" -> mutationCounts["Dead in Shell"] =
                    mutationCounts["Dead in Shell"]!! + 1

                "Moved" -> mutationCounts["Moved"] = mutationCounts["Moved"]!! + 1
                "Laid" -> mutationCounts["Laid"] = mutationCounts["Laid"]!! + 1
                else -> mutationCounts["Dead Before Moving To Nursery"] =
                    mutationCounts["Dead Before Moving To Nursery"]!! + 1
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
                val mutationCounts2 = MutationCounts[mutationKey2]!!

                when (status) {
                    "Incubating" -> mutationCounts2["Incubating"] =
                        mutationCounts2["Incubating"]!! + 1

                    "Hatched" -> mutationCounts2["Hatched"] = mutationCounts2["Hatched"]!! + 1
                    "Not Fertilized" -> mutationCounts2["Not Fertilized"] =
                        mutationCounts2["Not Fertilized"]!! + 1

                    "Broken" -> mutationCounts2["Broken"] = mutationCounts2["Broken"]!! + 1
                    "Abandon" -> mutationCounts2["Abandon"] = mutationCounts2["Abandon"]!! + 1
                    "Dead in Shell" -> mutationCounts2["Dead in Shell"] =
                        mutationCounts2["Dead in Shell"]!! + 1

                    "Moved" -> mutationCounts2["Moved"] = mutationCounts2["Moved"]!! + 1
                    "Laid" -> mutationCounts2["Laid"] = mutationCounts2["Laid"]!! + 1
                    else -> mutationCounts2["Dead Before Moving To Nursery"] =
                        mutationCounts2["Dead Before Moving To Nursery"]!! + 1
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
                barChart.animateXY(2000, 2000);
                val yAxisLeft = barChart.axisLeft
                yAxisLeft.isEnabled = true

                val yAxisRight = barChart.axisRight
                yAxisRight.isEnabled = true


                val xAxis = barChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.labelCount = barEntries.size

                xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                data.setValueFormatter(BarValueFormatter2(MutationCounts))

                barChart.invalidate()
            }
    }
    class BarValueFormatter2(private val combinedMutationCounts: Map<String, MutableMap<String, Int>>) : ValueFormatter() {
        override fun getBarLabel(barEntry: BarEntry?): String {
            val index = barEntry?.x?.toInt() ?: 0
            val mutationCombination = barEntry?.x?.toInt()?.let { combinedMutationCounts.keys.elementAt(it) }
            val counts = mutationCombination?.let { combinedMutationCounts[it] }
            val available = counts?.get("Incubating") ?: 0
            val forsale = counts?.get("Hatched") ?: 0
            val sold = counts?.get("Not Fertilized") ?: 0
            val deceased = counts?.get("Broken") ?: 0
            val lost = counts?.get("Abandon") ?: 0
            val exchanged = counts?.get("Dead in Shell") ?: 0
            val moved = counts?.get("Moved") ?: 0
            val laid = counts?.get("Laid") ?: 0
            val other = counts?.get("Dead Before Moving To Nursery") ?: 0

            return "Available: $available, For Sale: $forsale, Sold: $sold, Deceased: $deceased, " +
                    "Lost: $lost, Exchanged: $exchanged, Other: $other, Moved: $moved, Laid: $laid"
        }
    }
    private fun setupBarChart(barChart: BarChart, birdList: List<BirdBarChart>) {


        val combinedMutationCounts = mutableMapOf<String, MutableMap<String, Int>>()

        for (birdBarChart in birdList) {
            val combinedMutations = birdBarChart.mutations ?: "Mutation: None"
            val sex = birdBarChart.gender ?: "Unknown"

            if (combinedMutations != "Mutation: None") {
                if (!combinedMutationCounts.containsKey(combinedMutations)) {
                    combinedMutationCounts[combinedMutations] = mutableMapOf("Male" to 0, "Female" to 0, "Unknown" to 0)
                }

                val mutationCounts = combinedMutationCounts[combinedMutations]!!

                when (sex) {
                    "Male" -> mutationCounts["Male"] = mutationCounts["Male"]!! + 1
                    "Female" -> mutationCounts["Female"] = mutationCounts["Female"]!! + 1
                    else -> mutationCounts["Unknown"] = mutationCounts["Unknown"]!! + 1
                }
            }
        }

        val barEntries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        combinedMutationCounts.entries.forEachIndexed { index, entry ->
            val mutationCombination = entry.key
            val counts = entry.value
            val barEntry = BarEntry(index.toFloat(), floatArrayOf(counts["Male"]!!.toFloat(), counts["Female"]!!.toFloat(), counts["Unknown"]!!.toFloat()))
            barEntries.add(barEntry)
            labels.add(mutationCombination)
        }
        val legend = barChart.legend
        legend.form = Legend.LegendForm.CIRCLE
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.isWordWrapEnabled = true
        val dataSet = BarDataSet(barEntries,"")

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
        barChart.animateXY(2000, 2000);
        val yAxisLeft = barChart.axisLeft
        yAxisLeft.isEnabled = true

        val yAxisRight = barChart.axisRight
        yAxisRight.isEnabled = true


        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = barEntries.size

        // Set the custom XAxis value formatter to display the combinedMutations as labels
        xAxis.valueFormatter = CombinedMutationXAxisValueFormatter(combinedMutationCounts)
        // Set data value formatter to show counts
        data.setValueFormatter(BarValueFormatter(combinedMutationCounts))

        barChart.invalidate()
    }
    class CombinedMutationXAxisValueFormatter(private val combinedMutationCounts: Map<String, MutableMap<String, Int>>) : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            val mutationCombination = combinedMutationCounts.keys.elementAtOrNull(index)
            return mutationCombination ?: ""
        }
    }
    class BarValueFormatter(private val combinedMutationCounts: Map<String, MutableMap<String, Int>>) : ValueFormatter() {
        override fun getBarLabel(barEntry: BarEntry?): String {
            val index = barEntry?.x?.toInt() ?: 0
            val mutationCombination = barEntry?.x?.toInt()?.let { combinedMutationCounts.keys.elementAt(it) }
            val counts = mutationCombination?.let { combinedMutationCounts[it] }
            val maleCount = counts?.get("Male") ?: 0
            val femaleCount = counts?.get("Female") ?: 0
            val unknownCount = counts?.get("Unknown") ?: 0

            return "M: $maleCount, F: $femaleCount, U: $unknownCount"
        }
    }


    private fun setupStatusBarChart(barChart: BarChart, birdList: List<BirdBarChart>) {
        val combinedMutationCounts = mutableMapOf<String, MutableMap<String, Int>>()

        for (birdBarChart in birdList) {
            val combinedMutations = birdBarChart.mutations ?: "Mutation: None"
            val status = birdBarChart.status ?: "Paired"

            if (combinedMutations != "Mutation: None") {
                if (!combinedMutationCounts.containsKey(combinedMutations)) {
                    combinedMutationCounts[combinedMutations] = mutableMapOf("Available" to 0, "For Sale" to 0,
                        "Sold" to 0,"Deceased" to 0,"Exchanged" to 0,"Lost" to 0,"Donated" to 0,"Other" to 0,"Paired" to 0, )
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
        }

        val barEntries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        combinedMutationCounts.entries.forEachIndexed { index, entry ->
            val mutationCombination = entry.key
            val counts = entry.value
            val barEntry = BarEntry(index.toFloat(), floatArrayOf(
                counts["Available"]!!.toFloat(),
                counts["For Sale"]!!.toFloat(),
                counts["Sold"]!!.toFloat(),
                counts["Deceased"]!!.toFloat(),
                counts["Exchanged"]!!.toFloat(),
                counts["Lost"]!!.toFloat(),
                counts["Donated"]!!.toFloat(),
                counts["Other"]!!.toFloat(),
                counts["Paired"]!!.toFloat(),
            ))
            barEntries.add(barEntry)
            labels.add(mutationCombination)
        }
        val legend = barChart.legend
        legend.form = Legend.LegendForm.CIRCLE
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.isWordWrapEnabled = true
        val dataSet = BarDataSet(barEntries,"")

        val labels1 = listOf("Available", "For Sale", "Sold", "Deceased", "Exchanged", "Lost", "Donated", "Other", "Paired")

        dataSet.stackLabels = labels1.toTypedArray()
        dataSet.colors = listOf(Color.GREEN,Color.BLUE,Color.RED,Color.BLACK,Color.CYAN,Color.MAGENTA,Color.YELLOW,Color.LTGRAY, Color.rgb(255, 182, 193))


        val data = BarData(dataSet)
        barChart.data = data
        barChart.description.isEnabled = false
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.setPinchZoom(false)
        barChart.isDoubleTapToZoomEnabled = false
        barChart.animateXY(2000, 2000);
        val yAxisLeft = barChart.axisLeft
        yAxisLeft.isEnabled = true

        val yAxisRight = barChart.axisRight
        yAxisRight.isEnabled = true


        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = barEntries.size

        // Set the custom XAxis value formatter to display the combinedMutations as labels
        xAxis.valueFormatter = CombinedMutationXAxisValueFormatter(combinedMutationCounts)
        // Set data value formatter to show counts
        data.setValueFormatter(BarValueFormatter1(combinedMutationCounts))

        barChart.invalidate()
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

    private fun setupGenderBarChart(barChart: BarChart, genderList: List<BirdGenderBarChart>) {
        val barEntries = ArrayList<BarEntry>()

        // Initialize counters for each gender
        var maleCount = 0
        var femaleCount = 0
        var unknownCount = 0

        // Count the occurrences of each gender in the data
        for (birdData in genderList) {
            when (birdData.gender) {
                "Male" -> maleCount++
                "Female" -> femaleCount++
                "Unknown" -> unknownCount++
            }
        }

        barEntries.add(BarEntry(1f, maleCount.toFloat()))
        barEntries.add(BarEntry(2f, femaleCount.toFloat()))
        barEntries.add(BarEntry(3f, unknownCount.toFloat()))

        val dataSet = BarDataSet(barEntries, "Gender Counts")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }

//        val barChart: BarChart = view.findViewById(R.id.birdsbarChart)
//
//        // Create some sample data for the bar chart
//        val entries = arrayListOf(
//            BarEntry(1f, 10f),
//            BarEntry(2f, 15f),
//            BarEntry(3f, 8f),
//        )
//
//        val dataSet = BarDataSet(entries, "Sample Data")
//        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
//        val barData = BarData(dataSet)
//
//        barChart.data = barData
//        barChart.setFitBars(true)
//        barChart.description.isEnabled = false
//        barChart.animateY(1000)



    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }

}