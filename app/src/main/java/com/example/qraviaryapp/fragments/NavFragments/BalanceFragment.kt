package com.example.qraviaryapp.fragments.NavFragments

import BirdData
import ExpensesData
import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.qraviaryapp.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat

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
        totalPurchases= view.findViewById(R.id.purchase)
        totalReceive = view.findViewById(R.id.receive)
        dateFrom = view.findViewById(R.id.btndatefrom)
        dateTo = view.findViewById(R.id.btndateto)

        mAuth = FirebaseAuth.getInstance()
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
                    totalReceive.text = "₱"+decimalFormat.format(totalReceiveValue)
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
                totalExpenses.text = "Expenses: ₱"+decimalFormat.format(totalExpensesValue)
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
                        val price= itemSnapshot.child("Buy Price").value
                        val date = itemSnapshot.child("Bought On").value.toString()


                        val dateValue = date.toString()
                        val priceValue = price.toString().toDouble()
                        totalPurchasesValue += priceValue
                    }


                }

                totalPurchases.text = "Purchase: ₱"+decimalFormat.format(totalPurchasesValue)
                calculateTotalBalance()
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })


        return view
    }
    private fun calculateTotalBalance() {
        totalSpentValue = totalPurchasesValue + totalExpensesValue
        totalSpent.text = "₱"+decimalFormat.format(totalSpentValue)

        totalBalanceValue = totalReceiveValue - totalSpentValue
        totalBalance.text = "₱"+decimalFormat.format(totalBalanceValue)
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