package com.example.qraviaryapp.adapter

import ExpensesData
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class CategoryFragmentAdapter(
    val context: Context,
    private val dataList: MutableList<ExpensesData>
) : RecyclerView.Adapter<CategoryFragmentViewHolder>() {
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFragmentViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gene, parent, false)

        var originalDatalist = dataList

        return CategoryFragmentViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: CategoryFragmentViewHolder, position: Int) {
        val category = dataList[position]
        Auth = FirebaseAuth.getInstance()
        currentUser = Auth.currentUser?.uid.toString()
        holder.geneTextView.text = category.expenses



        holder.options.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)

            // Set the dialog message and buttons
            alertDialogBuilder.setMessage("Delete Category")
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val db =
                    FirebaseDatabase.getInstance().getReference("Users").child("ID: $currentUser")
                        .child("Category").child(category.expensesId.toString()).removeValue()

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

    fun updateDataList(newDataList: List<ExpensesData>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
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

class CategoryFragmentViewHolder(itemView: View, private val dataList: MutableList<ExpensesData>) :
    RecyclerView.ViewHolder(itemView) {
    val geneTextView: TextView = itemView.findViewById(R.id.geneTextView)
    val options: ImageView = itemView.findViewById(R.id.options)

    init {
        itemView.setOnClickListener {

        }
    }
}
