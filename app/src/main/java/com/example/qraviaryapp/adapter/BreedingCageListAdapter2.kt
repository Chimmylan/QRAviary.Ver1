package com.example.qraviaryapp.adapter

import CageData
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.CagesActivity.BreedingListActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CageListAdapter2(
    private val context: android.content.Context,
    private var dataList: MutableList<CageData>,
) : RecyclerView.Adapter<CageViewHolder2>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CageViewHolder2 {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cagelistbreeding, parent, false)


        return CageViewHolder2(view, dataList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: CageViewHolder2, position: Int) {
        val cage = dataList[position]

        val cageId = cage.cageId

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val itemRef = FirebaseDatabase.getInstance().reference.child("Users").child("ID: ${currentUserId.toString()}")
            .child("Cages").child(cageId.toString())
        holder.tvCage.text = cage.cage



//        if(!cage.cageBirdCount.isNullOrEmpty()){
//
//            holder.tvBirdCount.text = "Birds Count: ${cage.cageBirdCount}"
//        }else{
//            holder.tvBirdCount.text = "Birds Count: 0"
//        }

        if (!cage.cagePairBirdCount.isNullOrEmpty()){
            holder.tvCock.visibility = View.VISIBLE
            holder.tvHen.visibility = View.VISIBLE
            holder.tvCock.text = "Cock: "+ cage.cagemalemutation
            holder.tvHen.text = "Hen: "+ cage.cagefemalemutation
        }else{
            holder.tvCock.visibility = View.GONE
            holder.tvHen.visibility = View.GONE
            holder.tvPair.text = "Pair: None"
        }


//        val cageName = cage.cage


    }


}

class CageViewHolder2(itemView: View, private val dataList: MutableList<CageData>) :
    RecyclerView.ViewHolder(itemView) {

    val tvCage: TextView = itemView.findViewById(R.id.tvCageList)
//    val tvBirdCount: TextView = itemView.findViewById(R.id.tvBirdCount)
    val tvPair: TextView = itemView.findViewById(R.id.tvPairs)
    val tvCock: TextView = itemView.findViewById(R.id.tvCock)
    val tvHen: TextView = itemView.findViewById(R.id.tvHen)
    val tvClutches: TextView = itemView.findViewById(R.id.tvClutches)
    init {
        itemView.setOnClickListener {
            val cageName =
                dataList[adapterPosition].cage // Retrieve the cage name from the data list
            val cageKey =
                dataList[adapterPosition].cageId
            val cageQR = dataList[adapterPosition].cageQR

            val intent = Intent(itemView.context, BreedingListActivity::class.java)
            intent.putExtra("CageName", cageName)
            intent.putExtra("CageKey", cageKey)
            intent.putExtra("CageQR", cageQR)
            itemView.context.startActivity(intent)
        }

    }


}
