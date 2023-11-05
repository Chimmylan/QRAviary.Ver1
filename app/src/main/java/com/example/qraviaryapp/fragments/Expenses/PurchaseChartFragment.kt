package com.example.qraviaryapp.fragments.Expenses

import BirdData
import DateTotalExpense
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.example.qraviaryapp.adapter.MyMarker
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpensesChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PurchaseChartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var lineDataSet: LineDataSet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var expensesList1: ArrayList<DateTotalExpense>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_purchase_chart, container, false)

        mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Purchase Items")



        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val uniqueCategories = HashSet<String>()

                expensesList1 = ArrayList()


                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    if (data != null) {
//                        val categoryitem = itemSnapshot.child("Category").value
                        val PriceName = itemSnapshot.child("Buy Price").value
                        val date = itemSnapshot.child("Bought On").value.toString()
                        val month = itemSnapshot.child("Month").value.toString().toDouble()
                        val year = itemSnapshot.child("Year").value.toString().toDouble()

//                        val category = categoryitem.toString()
                        val priceNameValue = PriceName.toString().toDouble()
                        val dateValue = date.toString()
                        val existingDate = expensesList1.find { it.month == month &&  it.year == year}

                        if (existingDate != null) {

                            existingDate.price = existingDate.price?.plus(priceNameValue)!!
//                            existingDate.date = date
                        } else {

                            expensesList1.add(DateTotalExpense(dateValue, priceNameValue,month,year))

                        }



                    }


                }

                val lineCharts = view.findViewById<LineChart>(R.id.lineChart)
                setupLineChart(lineCharts, expensesList1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return view
    }

    lateinit var entries: ArrayList<Entry>

    lateinit var lineData: LineData

    private fun setupLineChart(lineChart: LineChart, expensesList1: List<DateTotalExpense>) {
        if (!isAdded) {
            return
        }

        val maxLabelCount = 100
        entries = ArrayList()

        for ((index, expenseData) in expensesList1.withIndex()) {
            val amount = expenseData.price.toString().toFloat()
            val date = expenseData.date
            val dateFormatted = SimpleDateFormat("MMM dd yyyy").parse(date)?.time?.toFloat() ?: 0f
            entries.add(Entry(dateFormatted, amount))
            Log.d(ContentValues.TAG, "1")
        }


//        entries = ArrayList(sortEntriesByMonth(entries))
        entries.sortBy { it.x }
        lineChart.xAxis.apply {
            lineChart.xAxis.valueFormatter = LineChartXAxisValueFormatter()
            labelRotationAngle = 45f
            lineChart.axisRight.isEnabled = false
            mAxisMaximum = 100f
            mAxisMinimum = 0f

            isGranularityEnabled = true
            granularity = 1f
            val monthsPassed = calculateMonthsPassed(entries)
            val labelCount = if (monthsPassed <= maxLabelCount) {
                monthsPassed
            } else {
                maxLabelCount
            }
            setLabelCount(labelCount, true)
            val xOffset = 20f // Adjust this value as needed.
            setXOffset(xOffset)
        }
        lineChart.setScaleEnabled(false)
        lineChart.description.text = "Monthly Expenses"
        lineDataSet = LineDataSet(entries, "Bird Purchases")
        lineData = LineData(lineDataSet)
        lineDataSet.color = resources.getColor(R.color.purple_200)
        lineDataSet!!.valueTextColor = Color.BLUE
        lineDataSet!!.valueTextSize= 10f


//        lineChart.setTouchEnabled(false)
        lineDataSet.setDrawValues(false)
        lineChart.data = lineData

        val xAxis = lineChart.xAxis


        xAxis.position = XAxis.XAxisPosition.BOTTOM
        Log.d(ContentValues.TAG, entries.toString())

        xAxis.setAvoidFirstLastClipping(true)

        val customMarker = MyMarker(requireContext(), R.layout.custom_marker)
        lineChart.marker = customMarker


    }
    private fun calculateMonthsPassed(entries: List<Entry>): Int {
        if (entries.isEmpty()) {
            return 0
        }
        val firstDateMillis = entries.first().x.toLong()
        val lastDateMillis = entries.last().x.toLong()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = firstDateMillis
        val startDate = calendar.time
        calendar.timeInMillis = lastDateMillis
        val endDate = calendar.time

        val startCalendar = Calendar.getInstance()
        startCalendar.time = startDate
        val endCalendar = Calendar.getInstance()
        endCalendar.time = endDate

        // Calculate the number of months passed
        var monthsPassed = 0
        while (startCalendar.before(endCalendar) || startCalendar == endCalendar) {
            monthsPassed++
            startCalendar.add(Calendar.MONTH, 1)
        }

        return monthsPassed
    }

    class LineChartXAxisValueFormatter : IndexAxisValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val dateInMillis = value.toLong()
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = dateInMillis
            val month = SimpleDateFormat("MM", Locale.getDefault()).format(dateInMillis)
            val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(dateInMillis)

            return if (month == "01") {
                "$month $year"
            } else {
                month
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
         * @return A new instance of fragment ExpensesChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpensesChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}