package com.example.qraviaryapp.adapter

import CageData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.CagesActivity.FlightListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FlightCageListAdapter2(
    private val context: android.content.Context,
    private var dataList: MutableList<CageData>,

) : RecyclerView.Adapter<FlightCageViewHolder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightCageViewHolder2 {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cageflight, parent, false)


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
        if (cage.cageBirdsCount != null) {
            holder.tvCount.text = "Birds: ${cage.cageBirdsCount}"
        }else{
            holder.tvCount.text = "0 Birds"
        }
        val cageName = cage.cage



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

class FlightCageViewHolder2(itemView: View, private val dataList: MutableList<CageData>) :
    RecyclerView.ViewHolder(itemView) {

    val tvCage: TextView = itemView.findViewById(R.id.tvCageList)
    val tvCount: TextView = itemView.findViewById(R.id.tvBirdCount)
    val tvMaturing: TextView = itemView.findViewById(R.id.tvMaturing)
    val tvReadToMove: TextView = itemView.findViewById(R.id.tvMove)



    init {
        itemView.setOnClickListener {
            val cageName =
                dataList[adapterPosition].cage // Retrieve the cage name from the data list
            val cageKey =
                dataList[adapterPosition].cageId

            val intent = Intent(itemView.context, FlightListActivity::class.java)
            intent.putExtra("CageName", cageName)
            intent.putExtra("CageKey", cageKey)
            itemView.context.startActivity(intent)

        }

    }


}
