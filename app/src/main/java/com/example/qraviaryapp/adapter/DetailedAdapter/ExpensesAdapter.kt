package com.example.qraviaryapp.adapter.DetailedAdapter




import ExpensesData

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R


class ExpensesAdapter(
    private val context: Context,
    private val dataList: MutableList<ExpensesData>
)
    : RecyclerView.Adapter<ExpensesViewHolder>() {


    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_GENE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_expenses,parent,false)

        return ExpensesViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val expenses = dataList[position]
        val expensesPrice = expenses.price
        holder.geneTextView.text = expenses.expenses
        holder.PriceName.text = "₱" + expensesPrice
        holder.comment.text = expenses.expensesComment
        holder.date.text = expenses.expensesDate
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
}
