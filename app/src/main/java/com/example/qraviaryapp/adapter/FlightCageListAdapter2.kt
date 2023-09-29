package com.example.qraviaryapp.adapter

import CageData
import ClickListener
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.BreedingCagesListActivity
import com.example.qraviaryapp.activities.dashboards.FlightCagesListActivity
import com.example.qraviaryapp.activities.dashboards.NurseryCagesListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FlightCageListAdapter2(
    private val context: android.content.Context,
    private var dataList: MutableList<CageData>,

) : RecyclerView.Adapter<FlightCageViewHolder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightCageViewHolder2 {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cagelist, parent, false)


        return FlightCageViewHolder2(view, dataList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: FlightCageViewHolder2, position: Int) {
        val cage = dataList[position]

        val cageId = cage.cageId

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val itemRef = FirebaseDatabase.getInstance().reference.child("Users").child("ID: ${currentUserId.toString()}")
            .child("Cages").child(cageId.toString())
        holder.tvCage.text = cage.cage

        val cageName = cage.cage



        holder.cageDel.setOnClickListener{
            val alertDialog = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Cage")
                .setMessage("Are you sure you want to delete this Cage?")
                .setPositiveButton("Delete"){_,_->
                    itemRef.removeValue()
                    dataList.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", null)
                .create()
            alertDialog.show()
            true
        }

    }


}

class FlightCageViewHolder2(itemView: View, private val dataList: MutableList<CageData>) :
    RecyclerView.ViewHolder(itemView) {

    val tvCage: TextView = itemView.findViewById(R.id.tvCageList)
    val cageDel: TextView = itemView.findViewById(R.id.cageDelete)


    init {
        itemView.setOnClickListener {


        }

    }


}
