package com.example.qraviaryapp.fragments.Expenses

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

        val pieChart = view.findViewById<PieChart>(R.id.pieChart)

        val entries = listOf(
            PieEntry(40f, "Category A"),
            PieEntry(30f, "Category B"),
            PieEntry(20f, "Category C"),
            PieEntry(10f, "Category D")
        )

        val dataSet = PieDataSet(entries, "Sample Pie Chart")

        // Configure the PieDataSet (colors, labels, etc.)
        // dataSet.setColors(...)
        // dataSet.setSliceSpace(...)
        // dataSet.setValueTextSize(...)

        val pieData = PieData(dataSet)

        // Attach the PieData to the PieChart
        pieChart.data = pieData

        // Refresh the chart
        pieChart.invalidate()
        return view
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