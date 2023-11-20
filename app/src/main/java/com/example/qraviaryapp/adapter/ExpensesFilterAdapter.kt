package com.example.qraviaryapp.adapter

import ExpensesData
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ExpensesFilterAdapter(
    val context: Context,
    private val dataList: MutableList<ExpensesData>
) : RecyclerView.Adapter<ExpensesFilterFragmentViewHolder>() {
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
        return dataList[position].expenses?.substring(0, 1)?.toUpperCase() ?: ""
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpensesFilterFragmentViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_category_filter, parent, false)

        var originalDatalist = dataList

        return ExpensesFilterFragmentViewHolder(view, dataList)
    }

    fun clearCheckedItems() {
        for (item in dataList) {
            item.isChecked = true
        }
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ExpensesFilterFragmentViewHolder, position: Int) {
        val category = dataList[position]
        Auth = FirebaseAuth.getInstance()
        currentUser = Auth.currentUser?.uid.toString()
        holder.geneTextView.text = category.expenses

        holder.geneTextView.isChecked = true

        if (holder.geneTextView.isChecked) {
            checkboxClickListener?.invoke(position, true)
        }

        holder.geneTextView.setOnCheckedChangeListener { _, isChecked ->

            checkboxClickListener?.invoke(position, isChecked)

        }


    }

    fun updateDataList(newDataList: List<ExpensesData>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    var checkboxClickListener: ((Int, Boolean) -> Unit)? = null


    /*  class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
          private val headerTextView: TextView = itemView.findViewById(R.id.headerTextView)

          fun bind(header: String) {
              headerTextView.text = header
          }
      }*/
}

class ExpensesFilterFragmentViewHolder(
    itemView: View,
    private val dataList: MutableList<ExpensesData>
) :
    RecyclerView.ViewHolder(itemView) {
    val geneTextView: CheckBox = itemView.findViewById(R.id.category)


    init {
        itemView.setOnClickListener {

        }
    }
}
