package com.example.qraviaryapp.adapter.DetailedAdapter




import ExpensesData

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.Locale


class ExpensesAdapter(
    private val context: Context,
    private val dataList: MutableList<ExpensesData>
)
    : RecyclerView.Adapter<ExpensesViewHolder>() {
    private lateinit var currentUser: String
    private lateinit var Auth: FirebaseAuth

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_GENE = 1
    }
    fun getHeaderForPosition(position: Int): String {
        if (position < 0 || position >= dataList.size) {
            return ""
        }
        // Assuming dataList is sorted by mutation name
        return dataList[position].monthyr?.substring(0, 8) ?: ""
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_expenses,parent,false)

        return ExpensesViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val expenses = dataList[position]
        Auth = FirebaseAuth.getInstance()
        currentUser = Auth.currentUser?.uid.toString()

        val expensesPrice = expenses.price
        val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
        holder.geneTextView.text = expenses.expenses
        holder.PriceName.text = currencyFormat.format(expensesPrice)
        holder.comment.text = expenses.expensesComment
        holder.date.text = expenses.expensesDate
        holder.options.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)

            // Set the dialog message and buttons
            alertDialogBuilder.setMessage("Delete Expenses")
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val db =
                    FirebaseDatabase.getInstance().getReference("Users").child("ID: $currentUser")
                        .child("Expenses").child(expenses.expensesId.toString()).removeValue()
                dataList.removeAt(position)
                notifyDataSetChanged()
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                // Handle the "Cancel" button click, dismiss the dialog or any other action
                dialog.dismiss()
            }

            // Create and show the alert dialog
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
    override fun getItemCount(): Int {
        return dataList.size
    }


    /*  class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          private val headerTextView: TextView = itemView.findViewById(R.id.headerTextView)

          fun bind(header: String) {
              headerTextView.text = header
          }
      }*/
}
class ExpensesViewHolder(itemView: View, private val dataList: MutableList<ExpensesData>) : RecyclerView.ViewHolder(itemView) {
    val geneTextView: TextView = itemView.findViewById(R.id.geneTextView)
    val PriceName: TextView = itemView.findViewById(R.id.PriceName)
    val comment: TextView = itemView.findViewById(R.id.tvComment)
    val date: TextView = itemView.findViewById(R.id.date)
    val options: ImageView = itemView.findViewById(R.id.options)
}
