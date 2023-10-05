package com.example.qraviaryapp.adapter

import EggData
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.ClutchesDetailedActivity

class ClutchesListAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<EggData>
) : RecyclerView.Adapter<ClutchesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClutchesViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_clutcheslist, parent, false)

        return ClutchesViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: ClutchesViewHolder, position: Int) {
        val clutch = dataList[position]

        val eggCount = clutch.eggCount
        val incubatingCount = clutch.eggIncubating
        val laidCount = clutch.eggLaid
        val hatchedCount = clutch.eggHatched
        val notFertilizedCount = clutch.eggNotFertilized
        val brokenCount = clutch.eggBroken
        val abandonCount = clutch.eggAbandon
        val deadInShellCount = clutch.eggDeadInShell
        val deadBeforeMovingToNurseryCount = clutch.eggDeadBeforeMovingToNursery
        val incubateDate = clutch.eggIncubationStartDate
        val laidDate = clutch.eggLaidStartDate

        holder.tvTotal.text = eggCount

        // Set visibility and text for Incubating
        if (incubatingCount != null && incubatingCount.toInt() > 0) {
            holder.tvIncubating.visibility = View.VISIBLE
            holder.tvIncubating.text = "$incubatingCount Incubating"
            holder.tvDate.text = incubateDate
        } else {
            holder.tvIncubating.visibility = View.GONE
        }

        // Set visibility and text for Laid
        if (laidCount != null && laidCount.toInt() > 0) {
            holder.tvLaid.visibility = View.VISIBLE
            holder.tvLaid.text = "$laidCount Laid"
            holder.tvDate.text = laidDate
        } else {
            holder.tvLaid.visibility = View.GONE
        }

        // Set visibility and text for Hatched
        if (hatchedCount != null && hatchedCount.toInt() > 0) {
            holder.tvHatched.visibility = View.VISIBLE
            holder.tvHatched.text = "$hatchedCount Hatched"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvHatched.visibility = View.GONE
        }
        if (notFertilizedCount != null && notFertilizedCount.toInt() > 0) {
            holder.tvNotFertilized.visibility = View.VISIBLE
            holder.tvNotFertilized.text = "$notFertilizedCount Not Fertilized"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvNotFertilized.visibility = View.GONE
        }
        if (brokenCount != null && brokenCount.toInt() > 0) {
            holder.tvBroken.visibility = View.VISIBLE
            holder.tvBroken.text = "$brokenCount Broken"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvBroken.visibility = View.GONE
        }
        if (abandonCount != null && abandonCount.toInt() > 0) {
            holder.tvAbandon.visibility = View.VISIBLE
            holder.tvAbandon.text = "$abandonCount Abandon"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvAbandon.visibility = View.GONE
        }
        if (deadInShellCount != null && deadInShellCount.toInt() > 0) {
            holder.tvDeadInShell.visibility = View.VISIBLE
            holder.tvDeadInShell.text = "$deadInShellCount Dead In Shell"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvDeadInShell.visibility = View.GONE
        }
        if (deadBeforeMovingToNurseryCount != null && deadBeforeMovingToNurseryCount.toInt() > 0) {
            holder.tvDeadBeforeMoving.visibility = View.VISIBLE
            holder.tvDeadBeforeMoving.text = "$deadBeforeMovingToNurseryCount Dead Before Moving to Nursery"
            // You might want to set the date for hatched eggs here if applicable.
        } else {
            holder.tvDeadBeforeMoving.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

}

class ClutchesViewHolder(itemView: View, private val dataList: MutableList<EggData>) :
    RecyclerView.ViewHolder(itemView) {

    val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
    val tvIncubating: TextView = itemView.findViewById(R.id.tvIncubating)
    val tvLaid: TextView = itemView.findViewById(R.id.tvLaid)
    val tvHatched: TextView = itemView.findViewById(R.id.tvHatched)
    val tvNotFertilized: TextView = itemView.findViewById(R.id.tvNotFertilized)
    val tvAbandon: TextView = itemView.findViewById(R.id.tvAbandon)
    val tvBroken: TextView = itemView.findViewById(R.id.tvBroke)
    val tvDeadInShell: TextView = itemView.findViewById(R.id.tvDeadInShell)
    val tvDeadBeforeMoving: TextView = itemView.findViewById(R.id.tvDeadBeforeRinging)
    val tvDate: TextView = itemView.findViewById(R.id.tvDate)

    init {
        itemView.setOnClickListener {
            val bundle = Bundle()

            bundle.putString("PairKey", dataList[adapterPosition].pairKey)
            bundle.putString("EggKey", dataList[adapterPosition].eggKey)
            bundle.putString("PairFlightMaleKey", dataList[adapterPosition].pairFlightMaleKey)
            bundle.putString("PairFlightFemaleKey", dataList[adapterPosition].pairFlightFemaleKey)
            bundle.putString("PairMaleKey", dataList[adapterPosition].pairBirdMaleKey)
            bundle.putString("PairFemaleKey", dataList[adapterPosition].pairBirdFemaleKey)
            bundle.putString("PairMaleID", dataList[adapterPosition].pairMaleId)
            bundle.putString("PairFemaleID", dataList[adapterPosition].pairFemaleId)
            bundle.putString("CageKeyFemale", dataList[adapterPosition].eggcagekeyFemale)
            bundle.putString("CageKeyMale", dataList[adapterPosition].eggcagekeyMale)
            bundle.putString("CageBirdFemale", dataList[adapterPosition].eggcagebirdFemale)
            bundle.putString("CageBirdMale", dataList[adapterPosition].eggcagebirdMale)
            val i = Intent(itemView.context, ClutchesDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }

}