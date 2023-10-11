package com.example.qraviaryapp.fragments.Expenses

import DateTotalExpense
import ExpensesData
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qraviaryapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpensesChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpensesChartFragment : Fragment() {
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
    lateinit var expensesList: ArrayList<ExpensesData>
    lateinit var expensesList1: ArrayList<DateTotalExpense>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_expenses_chart, container, false)

        mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Expenses")
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)



        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val uniqueCategories = HashSet<String>()
                expensesList = ArrayList()
                expensesList1 = ArrayList()


                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(ExpensesData::class.java)
                    if (data != null) {
                        val categoryitem = itemSnapshot.child("Category").value
                        val PriceName = itemSnapshot.child("Amount").value
                        val date = itemSnapshot.child("Date").value
                        val year= itemSnapshot.child("Year").value
                        val category = categoryitem.toString()
                        val priceNameValue = PriceName.toString().toDouble()
                        val dateValue = date.toString().toDouble()
                        val dateyear = year.toString().toDouble()

                        val existingDate = expensesList1.find { it.date.toDouble() == dateValue && it.year.toDouble() == dateyear }

                        if (existingDate != null) {

                            existingDate.price = existingDate.price?.plus(priceNameValue)!!
                            existingDate.date = dateValue
                        } else {

                            expensesList1.add(DateTotalExpense(dateValue, priceNameValue, dateyear))

                        }

                        val existingCategory = expensesList.find { it.expenses == category }
                        if (existingCategory != null) {
                            // If it exists, add the value to the existing category
                            existingCategory.price = existingCategory.price?.plus(priceNameValue)
//                            existingCategory.date = dateValue
                        } else {
                            // If it doesn't exist, create a new category and add it to the list
                            expensesList.add(ExpensesData(category, priceNameValue, dateValue))

                        }

                    }


                }

                val pieCharts = view.findViewById<PieChart>(R.id.pieChart)
                setupPieChart(pieCharts, expensesList)
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
            // Fragment is not attached to a context, return or handle accordingly
            return
        }
        entries = ArrayList()

        for ((index, expenseData) in expensesList1.withIndex()) {
            val amount = expenseData.price.toString().toFloat()
            val date = expenseData.date.toString().toFloat() - 1
            entries.add(Entry(date, amount))
            Log.d(ContentValues.TAG, "1")
        }


        entries.sortBy { it.x }

        Log.d(ContentValues.TAG, entries.toString())
        lineChart.description.text = "Monthly Expenses"
        lineDataSet = LineDataSet(entries, "Expenses")
//        lineDataSet.setDrawValues(false)
        lineData = LineData(lineDataSet)
        lineDataSet.color = resources.getColor(R.color.purple_200)
        lineDataSet!!.valueTextColor = Color.BLUE
        lineDataSet!!.valueTextSize= 10f
        lineChart.data = lineData
        lineChart.xAxis.setLabelCount(entries.size, true)
        lineChart.axisRight.isEnabled = false
        val xAxis = lineChart.xAxis
//        xAxis.valueFormatter = YearMonthValueFormatter()

        xAxis.valueFormatter = object : ValueFormatter() {
            private val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()
                return if (index >= 0 && index < months.size) {
                    months[index]
                } else {
                    ""
                }
            }
        }

        xAxis.setAvoidFirstLastClipping(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM



    }
    class YearMonthValueFormatter : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val year = value.toInt() / 100
            val month = value.toInt() % 100
            return if (month == 1) {
                "$year"
            } else {
                // Display the month number for other months
                String.format("%02d", month)
            }
        }
    }



    private fun setupPieChart(pieChart: PieChart, expensesList: List<ExpensesData>) {
        val entries = ArrayList<PieEntry>()

        // Define a list of custom colors
        val colors = ArrayList<Int>()
        colors.add(Color.RED)
        colors.add(Color.GREEN)
        colors.add(Color.BLUE)
        colors.add(Color.YELLOW)
        colors.add(Color.CYAN)
        colors.add(Color.MAGENTA)
        colors.add(Color.GRAY)
        colors.add(Color.LTGRAY)
        colors.add(Color.DKGRAY)
        colors.add(Color.BLACK)
        colors.add(Color.WHITE)
        colors.add(Color.parseColor("#FF5733")) // Custom color 1
        colors.add(Color.parseColor("#33FF57")) // Custom color 2
        colors.add(Color.parseColor("#5733FF")) // Custom color 3
        colors.add(Color.parseColor("#FF33A1")) // Custom color 4
        colors.add(Color.parseColor("#33A1FF")) // Custom color 5
        colors.add(Color.parseColor("#A1FF33")) // Custom color 6
        colors.add(Color.parseColor("#33FFA1")) // Custom color 7
        colors.add(Color.parseColor("#A133FF")) // Custom color 8
        colors.add(Color.parseColor("#FF33FF")) // Custom color 9
        colors.add(Color.parseColor("#33FFFF")) // Custom color 10

        val dataSetColors = ArrayList<Int>()

        for ((index, expenseData) in expensesList.withIndex()) {


            val amount = expenseData.price.toString()
            entries.add(PieEntry(amount.toFloat(), expenseData.expenses))


            val colorIndex = index % colors.size
            dataSetColors.add(colors[colorIndex])
        }

        val dataSet = PieDataSet(entries, "Expenses")


        dataSet.colors = dataSetColors

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(android.R.color.transparent)
        pieChart.setTransparentCircleRadius(0f)
        pieChart.animateY(1000)
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