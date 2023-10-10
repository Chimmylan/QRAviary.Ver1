package com.example.qraviaryapp.fragments.Expenses

import ExpensesData
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qraviaryapp.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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

        val view = inflater.inflate(R.layout.fragment_expenses_chart, container, false)

        mAuth = FirebaseAuth.getInstance()
        val currentUserId = mAuth.currentUser?.uid
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Expenses")
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)



        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val expensesList = ArrayList<ExpensesData>()

                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(ExpensesData::class.java)

                    if (data != null) {
                        val key = itemSnapshot.key.toString()
                        data.expensesId = key
                        val categoryitem = itemSnapshot.child("Category").value
                        val PriceName = itemSnapshot.child("Amount").value
                        val date = itemSnapshot.child("Beginning").value
                        val comment = itemSnapshot.child("Comment").value
                        val category = categoryitem.toString()
                        val priceNameValue = PriceName.toString()
                        val dateValue = date.toString()
                        val commentValue = comment.toString()

                        data.expenses = category
                        data.price = priceNameValue
                        data.expensesComment = commentValue
                        data.expensesDate = dateValue

                            expensesList.add(data).toString()
                    }

                }
                val pieChart = view.findViewById<PieChart>(R.id.pieChart)
                setupPieChart(pieChart, expensesList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        return view
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