package com.example.qraviaryapp.adapter

import CageData
import ClickListener
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.NurseryCagesListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NurseryCageListAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<CageData>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<NurseryCageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NurseryCageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cagelist_category, parent, false)


        return NurseryCageViewHolder(view, dataList, context as NurseryCagesListActivity)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: NurseryCageViewHolder, position: Int) {
        val cage = dataList[position]

        val cageId = cage.cageId

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val itemRef = FirebaseDatabase.getInstance().reference.child("Users").child("ID: ${currentUserId.toString()}")
            .child("Cages").child(cageId.toString())
        holder.tvCage.text = cage.cage
        holder.tvCount.text = cage.cageBirdsCount

        val cageName = cage.cage


//
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

class NurseryCageViewHolder(itemView: View, private val dataList: MutableList<CageData>, private val activity: NurseryCagesListActivity) :
    RecyclerView.ViewHolder(itemView) {

    val tvCage: TextView = itemView.findViewById(R.id.tvCageList)
    val tvCount: TextView = itemView.findViewById(R.id.totalbirds)



    init {
        itemView.setOnClickListener {

            val cageName =
                dataList[adapterPosition].cage // Retrieve the cage name from the data list
            val cagekey = dataList[adapterPosition].cageId
            val intent = Intent()
            intent.putExtra("CageName", cageName)
            intent.putExtra("CageKey", cagekey)
            activity.setResult(Activity.RESULT_OK, intent)

            activity.finish()
        }

    }


}
