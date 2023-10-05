package com.example.qraviaryapp.adapter

import CageData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.CagesActivity.NurseryListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NurseryCageListAdapter2(
    private val context: android.content.Context,
    private var dataList: MutableList<CageData>
) : RecyclerView.Adapter<NurseryCageViewHolder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NurseryCageViewHolder2 {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cagelist, parent, false)


        return NurseryCageViewHolder2(view, dataList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: NurseryCageViewHolder2, position: Int) {
        val cage = dataList[position]

        val cageId = cage.cageId

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val itemRef = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}")
            .child("Cages").child(cageId.toString())
        holder.tvCage.text = cage.cage
        if (cage.cageBirdsCount != null) {
            holder.tvBirdCount.text = "Birds: ${cage.cageBirdsCount}"
        }else{
            holder.tvBirdCount.text = "0 Birds"
        }

        if (cage.cageBirdsMaturedCount != null){
            holder.tvReadToMove.visibility = View.VISIBLE
            holder.tvReadToMove.text = "Ready to Move: ${cage.cageBirdsMaturedCount}"
        }else{
            holder.tvReadToMove.text = "Ready to Move: 0"

        }

        if (cage.cageBirdsAvailCount != null){
            holder.tvMaturing.visibility = View.VISIBLE
            holder.tvMaturing.text = "Maturing: ${cage.cageBirdsAvailCount}"
        }else{
            holder.tvMaturing.text = "Maturing: 0"
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

class NurseryCageViewHolder2(itemView: View, private val dataList: MutableList<CageData>) :
    RecyclerView.ViewHolder(itemView) {

    val tvCage: TextView = itemView.findViewById(R.id.tvCageList)
    val tvBirdCount: TextView = itemView.findViewById(R.id.tvBirdCount)
    val tvMaturing: TextView = itemView.findViewById(R.id.tvMaturing)
    val tvReadToMove: TextView = itemView.findViewById(R.id.tvMove)


    init {
        itemView.setOnClickListener {
            val cageName =
                dataList[adapterPosition].cage // Retrieve the cage name from the data list
            val cageKey =
                dataList[adapterPosition].cageId

            val intent = Intent(itemView.context, NurseryListActivity::class.java)
            intent.putExtra("CageName", cageName)
            intent.putExtra("CageKey", cageKey)
            itemView.context.startActivity(intent)

        }

    }


}
