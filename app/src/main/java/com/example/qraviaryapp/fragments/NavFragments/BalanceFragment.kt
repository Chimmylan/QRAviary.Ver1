package com.example.qraviaryapp.fragments.NavFragments

import BirdData
import ExpensesData
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BalanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BalanceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var snackbar: Snackbar
    private lateinit var connectivityManager: ConnectivityManager
    private var isNetworkAvailable = true
    private lateinit var mAuth: FirebaseAuth
    private lateinit var totalBalance: TextView
    private lateinit var totalSpent: TextView
    private lateinit var totalReceive: TextView
    private lateinit var totalExpenses: TextView
    private lateinit var totalPurchases: TextView
    private lateinit var dateFrom: Button
    private lateinit var dateTo: Button
    var totalExpensesValue = 0.0
    var totalReceiveValue = 0.0
    var totalPurchasesValue = 0.0
    var totalSpentValue = 0.0
    var totalBalanceValue = 0.0
    private lateinit var datePickerDialogBeginning: DatePickerDialog
    private lateinit var datePickerDialogEnd: DatePickerDialog
    private var dateFromFormat: String? = null
    private var dateToFormat: String? = null
    private var fromDateSelected: String? = null
    private var toDateSelected: String? = null

    private val decimalFormat = DecimalFormat("#,###,##0")
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
        val view = inflater.inflate(R.layout.fragment_balance, container, false)
        totalBalance = view.findViewById(R.id.balance)
        totalSpent = view.findViewById(R.id.spent)
        totalExpenses = view.findViewById(R.id.expense)
        totalPurchases = view.findViewById(R.id.purchase)
        totalReceive = view.findViewById(R.id.receive)
        dateFrom = view.findViewById(R.id.btndatefrom)
        dateTo = view.findViewById(R.id.btndateto)
        initDatePickers()
        showDatePickerDialog(requireContext(), dateFrom, datePickerDialogBeginning)
        showDatePickerDialog(requireContext(), dateTo, datePickerDialogEnd)
        mAuth = FirebaseAuth.getInstance()


        defaultBalance()



        return view
    }

    private fun defaultBalance() {
        val currentUserId = mAuth.currentUser?.uid
        val PurchasesRef =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Purchase Items")
        val ExpensesRef =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Expenses")
        val ReceiveRef =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Sold Items")

        ReceiveRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    if (data != null) {
                        val price = itemSnapshot.child("Sale Price").value
                        val date = itemSnapshot.child("Sold Date").value.toString()

                        val dateValue = date.toString()
                        val priceValue = price.toString().toDouble()

                        totalReceiveValue += priceValue

                    }

                }
                totalReceive.text = "₱" + decimalFormat.format(totalReceiveValue)
                calculateTotalBalance()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        ExpensesRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(ExpensesData::class.java)
                    if (data != null) {
                        val price = itemSnapshot.child("Amount").value
                        val date = itemSnapshot.child("Beginning").value.toString()

                        val dateValue = date.toString()
                        val priceValue = price.toString().toDouble()
                        totalExpensesValue += priceValue
                    }
                }
                totalExpenses.text = "Expenses: ₱" + decimalFormat.format(totalExpensesValue)
                calculateTotalBalance()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        PurchasesRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    if (data != null) {
                        val price = itemSnapshot.child("Buy Price").value
                        val date = itemSnapshot.child("Bought On").value.toString()


                        val dateValue = date.toString()
                        val priceValue = price.toString().toDouble()
                        totalPurchasesValue += priceValue
                    }


                }

                totalPurchases.text = "Purchase: ₱" + decimalFormat.format(totalPurchasesValue)
                calculateTotalBalance()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

    }

    private fun applyDataRangeFilter() {
        if (fromDateSelected != null && toDateSelected != null) {
            // Convert date strings to Calendar objects for comparison
            val fromCalendar = convertDateStringToCalendar(fromDateSelected!!)
            val toCalendar = convertDateStringToCalendar(toDateSelected!!)

            // Check if 'From' date is later than 'To' date
            if (fromCalendar.after(toCalendar)) {
                // Show error toast
                showToast("Invalid date range. 'From' date should be earlier than 'To' date.")
                return
            }

        totalBalanceValue = 0.0
        totalSpentValue = 0.0
        totalReceiveValue = 0.0
        totalExpensesValue = 0.0
        totalPurchasesValue = 0.0
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val currentUserId = mAuth.currentUser?.uid
        val PurchasesRef =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Purchase Items")
        val ExpensesRef =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Expenses")
        val ReceiveRef =
            FirebaseDatabase.getInstance().getReference("Users/ID: $currentUserId/Sold Items")
        if (toDateSelected == null) {
            // If toDateSelected is not specified, set it to the current date or a future date
            toDateSelected = makeDateString(
                currentDay,
                currentMonth + 1,
                currentYear
            ) // This assumes you have a makeDateString function
        }

        val recieve =
            ReceiveRef.orderByChild("Sold Date").startAt(fromDateSelected).endAt(toDateSelected)
        val purchase =
            PurchasesRef.orderByChild("Bought On").startAt(fromDateSelected).endAt(toDateSelected)
        val expenses =
            ExpensesRef.orderByChild("Beginning").startAt(fromDateSelected).endAt(toDateSelected)


        recieve.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    if (data != null) {
                        val price = itemSnapshot.child("Sale Price").value
                        val date = itemSnapshot.child("Sold Date").value.toString()


                        val dateValue = date.toString()
                        val priceValue = price.toString().toDouble()
                        Log.d(TAG, "Recieve: $priceValue")
                        totalReceiveValue += priceValue

                    }

                }
                totalReceive.text = "₱" + decimalFormat.format(totalReceiveValue)
                calculateTotalBalance()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        expenses.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(ExpensesData::class.java)
                    if (data != null) {
                        val price = itemSnapshot.child("Amount").value
                        val date = itemSnapshot.child("Beginning").value.toString()

                        val dateValue = date.toString()
                        val priceValue = price.toString().toDouble()
                        Log.d(TAG, "Expenses: $priceValue")
                        totalExpensesValue += priceValue
                    }
                }
                totalExpenses.text = "Expenses: ₱" + decimalFormat.format(totalExpensesValue)
                calculateTotalBalance()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        purchase.addValueEventListener(object : ValueEventListener {
            @SuppressLint("CutPasteId")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (itemSnapshot in dataSnapshot.children) {
                    val data = itemSnapshot.getValue(BirdData::class.java)
                    if (data != null) {
                        val price = itemSnapshot.child("Buy Price").value
                        val date = itemSnapshot.child("Bought On").value.toString()

                        val dateValue = date.toString()
                        val priceValue = price.toString().toDouble()
                        Log.d(TAG, "Purchase: $priceValue")
                        totalPurchasesValue += priceValue
                    }


                }

                totalPurchases.text = "Purchase: ₱" + decimalFormat.format(totalPurchasesValue)
                calculateTotalBalance()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun convertDateStringToCalendar(dateString: String): Calendar {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
        cal.time = sdf.parse(dateString)!!
        return cal
    }


    private fun calculateTotalBalance() {
        totalSpentValue = totalPurchasesValue + totalExpensesValue
        totalSpent.text = "₱" + decimalFormat.format(totalSpentValue)
        totalBalanceValue = totalReceiveValue - totalSpentValue
        totalBalance.text = "₱" + decimalFormat.format(totalBalanceValue)
    }

    private fun initDatePickers() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        // Set the maximum date to today
        datePickerDialogBeginning = DatePickerDialog(
            requireContext(), AlertDialog.THEME_HOLO_LIGHT,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                dateFromFormat = makeDateString(day, month + 1, year)
                dateFrom.text = dateFromFormat
                fromDateSelected = dateFrom.text.toString()
                applyDataRangeFilter()
            },
            year, month, day
        )
        datePickerDialogBeginning.datePicker.maxDate = cal.timeInMillis

        datePickerDialogEnd = DatePickerDialog(
            requireContext(), AlertDialog.THEME_HOLO_LIGHT,
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                dateToFormat = makeDateString(day, month + 1, year)
                dateTo.text = dateToFormat
                toDateSelected = dateTo.text.toString()
                applyDataRangeFilter()
            },
            year, month, day
        )
        datePickerDialogEnd.datePicker.maxDate = cal.timeInMillis
    }


    fun showDatePickerDialog(context: Context, button: Button, datePickerDialog: DatePickerDialog) {
        button.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN" // Default should never happen
        }
    }

    private fun showSnackbar(message: String) {
        snackbar.setText(message)
        snackbar.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BalanceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BalanceFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}