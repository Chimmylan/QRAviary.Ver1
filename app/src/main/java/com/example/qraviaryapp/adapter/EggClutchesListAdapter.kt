package com.example.qraviaryapp.adapter

import EggData
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.EditActivities.EditEggActivity

class EggClutchesListAdapter(
    private val context: Context,
    private val dataList: MutableList<EggData>
) : RecyclerView.Adapter<EggClutchesHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EggClutchesHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_eggclutch, parent, false)

        return EggClutchesHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: EggClutchesHolder, position: Int) {
        val eggs = dataList[position]

        holder.tvStatus.text = eggs.eggStatus
        holder.tvDate.text = eggs.eggDate
        // Check the egg status and set the date TextView visibility accordingly
        when (eggs.eggStatus) {
            "Incubating", "Laid", "Hatched" -> {
                holder.tvTime.text = eggs.eggDate
                holder.tvTime.visibility = View.VISIBLE
            }
            else -> {
                // Hide the date TextView for other statuses
                holder.tvTime.visibility = View.GONE
            }
        }


    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class EggClutchesHolder(itemvView: View, private val dataList: MutableList<EggData>) :
    RecyclerView.ViewHolder(itemvView) {
    val tvStatus: TextView = itemvView.findViewById(R.id.tvStatus)
    val tvDate: TextView = itemvView.findViewById(R.id.tvDate)
    val tvTime: TextView = itemvView.findViewById(R.id.tvTime)

    init {


        itemView.setOnClickListener {
            val incubatingStartDate = dataList[adapterPosition].eggIncubationStartDate
            val maturingStartDate = dataList[adapterPosition].eggMaturingStartDate
            val eggKey = dataList[adapterPosition].eggKey
            val individualEggKey = dataList[adapterPosition].individualEggKey
            val pairKey = dataList[adapterPosition].pairKey


            val i = Intent(itemvView.context, EditEggActivity::class.java)
            i.putExtra("IncubatingStartDate", incubatingStartDate)
            i.putExtra("MaturingStartDate", maturingStartDate)
            i.putExtra("EggKey", eggKey)
            i.putExtra("IndividualEggKey", individualEggKey)
            i.putExtra("PairKey", pairKey)
            itemvView.context.startActivity(i)

        }
    }
}