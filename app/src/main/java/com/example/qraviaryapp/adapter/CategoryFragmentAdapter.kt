package com.example.qraviaryapp.adapter

import BirdData
import ClickListener
import ExpensesData
import MutationData
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.ChooseCategoryActivity
import com.example.qraviaryapp.activities.dashboards.HomeMutationActivity
import com.example.qraviaryapp.activities.dashboards.MutationsActivity

class CategoryFragmentAdapter(
    private val context: Context,
    private val dataList: MutableList<ExpensesData>
)
    : RecyclerView.Adapter<CategoryFragmentViewHolder>() {


    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_GENE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFragmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gene,parent,false)

        return CategoryFragmentViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: CategoryFragmentViewHolder, position: Int) {
        val category = dataList[position]

        holder.geneTextView.text = category.expenses

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
class CategoryFragmentViewHolder(itemView: View, private val dataList: MutableList<ExpensesData>) : RecyclerView.ViewHolder(itemView) {
    val geneTextView: TextView = itemView.findViewById(R.id.geneTextView)

    init {
        itemView.setOnClickListener {

        }
    }
}
