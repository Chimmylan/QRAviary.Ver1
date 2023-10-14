package com.example.qraviaryapp.fragments.NavFragments

import BirdBarChart
import BirdData
import BirdGenderBarChart
import BirdStatusBarChart
import ExpensesData
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                genderList = ArrayList()
                statusList = ArrayList()
                birdList = ArrayList()
                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    if (data != null) {
                        val gender = itemSnapshot.child("Gender").value
                        val status = itemSnapshot.child("Status").value
                        val mutation1 = itemSnapshot.child("Mutation1").value
                        val mutation2 = itemSnapshot.child("Mutation2").value
                        val mutation3 = itemSnapshot.child("Mutation3").value
                        val mutation4 = itemSnapshot.child("Mutation4").value
                        val mutation5 = itemSnapshot.child("Mutation5").value
                        val mutation6 = itemSnapshot.child("Mutation6").value

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
                        val birdBarChart= BirdBarChart(CombinedMutations,sex)
                        genderList.add(birdGenderBarChart)
                        statusList.add(birdStatusBarChart)
                        birdList.add(birdBarChart)
                    }
                }

                val birdGenderChart = view.findViewById<BarChart>(R.id.birdsgenderChart)
                setupGenderBarChart(birdGenderChart, genderList)
                val birdStatusChart = view.findViewById<BarChart>(R.id.birdstatusChart)
                setupStatusBarChart(birdStatusChart, statusList)
                val birdChart = view.findViewById<BarChart>(R.id.birdsChart)
                setupBarChart(birdChart, birdList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return view
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

        val yAxisLeft = barChart.axisLeft
        yAxisLeft.isEnabled = true

        val yAxisRight = barChart.axisRight
        yAxisRight.isEnabled = true

        val legend = barChart.legend
        legend.form = Legend.LegendForm.CIRCLE
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER

        // Set data value formatter to show counts
        data.setValueFormatter(BarValueFormatter(combinedMutationCounts))

        barChart.invalidate()
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


    private fun setupStatusBarChart(barChart: BarChart, statusList: List<BirdStatusBarChart>) {
        val barEntries = ArrayList<BarEntry>()
        var available = 0
        var forsale = 0
        var sold= 0
        var deceased = 0
        var exchanged = 0
        var lost= 0
        var donated= 0
        var other = 0
        var paired = 0

        for (birdData in statusList) {
            when (birdData.status) {
                "Available" -> available++
                "For Sale" -> forsale++
                "Sold" -> sold++
                "Deceased" -> deceased++
                "Exchanged" -> exchanged++
                "Lost" -> lost++
                "Donated" -> donated++
                "Other" -> other++
                "Paired" -> paired++
            }
        }

        barEntries.add(BarEntry(1f, available.toFloat()))
        barEntries.add(BarEntry(2f, forsale.toFloat()))
        barEntries.add(BarEntry(3f, sold.toFloat()))
        barEntries.add(BarEntry(4f, deceased.toFloat()))
        barEntries.add(BarEntry(5f, exchanged.toFloat()))
        barEntries.add(BarEntry(6f, lost.toFloat()))
        barEntries.add(BarEntry(7f, donated.toFloat()))
        barEntries.add(BarEntry(8f, other.toFloat()))
        barEntries.add(BarEntry(9f, paired.toFloat()))

        val dataSet = BarDataSet(barEntries, "Status Counts")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        val barData = BarData(dataSet)

        barChart.data = barData
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()

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