package com.example.qraviaryapp.adapter

import CageData
import ClickListener
import PairData
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.AddActivities.GiveFosterActivity
import com.example.qraviaryapp.activities.dashboards.BreedingCagesListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FosterAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<PairData>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<CageFosterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CageFosterViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_pairlist, parent, false)


        return CageFosterViewHolder(view, dataList, context as GiveFosterActivity)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CageFosterViewHolder, position: Int) {
        val cage = dataList[position]




//        holder.cageDel.setOnClickListener{
//            val alertDialog = AlertDialog.Builder(holder.itemView.context)
//                .setTitle("Delete Cage")
//                .setMessage("Are you sure you want to delete this Cage?")
//                .setPositiveButton("Delete"){_,_->
//                    itemRef.removeValue()
//                    dataList.removeAt(position)
//                    notifyDataSetChanged()
//                }
//                .setNegativeButton("Cancel", null)
//                .create()
//            alertDialog.show()
//            true
//        }

    }


}

class CageFosterViewHolder(itemView: View, private val dataList: MutableList<PairData>, private val activity: GiveFosterActivity) :
    RecyclerView.ViewHolder(itemView) {

    val tvCage: TextView = itemView.findViewById(R.id.tvCageList)
    val tvCount: TextView = itemView.findViewById(R.id.totalbirds)



    init {
        itemView.setOnClickListener {



            activity.finish()
        }

    }


}
