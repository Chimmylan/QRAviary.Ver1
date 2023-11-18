package com.example.qraviaryapp.adapter

import MutationData
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

class HomeGenesAdapter(
    val context: Context,
    private val dataList: MutableList<MutationData>
)
    : RecyclerView.Adapter<HomeGeneViewHolder>() {
    private lateinit var currentUser: String
    private lateinit var Auth: FirebaseAuth

    fun getHeaderForPosition(position: Int): String {
        if (position < 0 || position >= dataList.size) {
            return ""
        }
        // Assuming dataList is sorted by mutation name
        return dataList[position].mutations?.substring(0, 1)?.toUpperCase() ?: ""
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeGeneViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gene,parent,false)

        return HomeGeneViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: HomeGeneViewHolder, position: Int) {
        val mutation = dataList[position]
        Auth = FirebaseAuth.getInstance()
        currentUser = Auth.currentUser?.uid.toString()
        holder.geneTextView.text = mutation.mutations
        holder.options.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(context)

            // Set the dialog message and buttons
            alertDialogBuilder.setMessage("Delete Mutation")
            alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                val db =
                    FirebaseDatabase.getInstance().getReference("Users").child("ID: $currentUser")
                        .child("Mutations").child(mutation.mutationsId.toString()).removeValue()

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



}
class HomeGeneViewHolder(itemView: View, private val dataList: MutableList<MutationData>) : RecyclerView.ViewHolder(itemView) {
    val geneTextView: TextView = itemView.findViewById(R.id.geneTextView)

    val options: ImageView = itemView.findViewById(R.id.options)
}
