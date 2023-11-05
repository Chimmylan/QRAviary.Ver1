package com.example.qraviaryapp.adapter.DetailedAdapter

import BirdData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R

class PairingAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<BirdData>
) : RecyclerView.Adapter<PairingsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairingsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_birdlist, parent, false)

        return PairingsViewHolder(view, dataList)
    }

    override fun onBindViewHolder(holder: PairingsViewHolder, position: Int) {
        val pair = dataList[position]

        holder.tvIdentifier.text = pair.identifier
        val mutationList = listOf(
            pair.mutation1,
            pair.mutation2,
            pair.mutation3,
            pair.mutation4,
            pair.mutation5,
            pair.mutation6
        )

        val nonNullMutations = mutationList.filter { !it.isNullOrBlank() }

        val combinedMutations = if (nonNullMutations.isNotEmpty()) {
            "Mutation: " + nonNullMutations.joinToString(" / ")
        } else {
            "Mutation: None"
        }

        holder.tvMutation.text = combinedMutations
        holder.tvStatus.text = pair.status


        if (pair.status == "Available" || pair.status == "For Sale") {
            val cageInfo = when {
                pair.status == "Available" -> pair.availCage
                pair.status == "For Sale" -> pair.forSaleCage
                else -> ""
            }

            if (cageInfo.isNullOrBlank()) {
                holder.tvCage.visibility = View.GONE
            } else {
                holder.tvCage.visibility = View.VISIBLE
                holder.tvCage.text = "Cage: $cageInfo"
            }
        } else {
            holder.tvCage.visibility = View.GONE
        }

        val genderIcon = when (pair.gender) {
            "Male" -> {
                R.drawable.baseline_male_24
            }

            "Female" -> {
                R.drawable.female_logo
            }

            else -> {
                R.drawable.unknown
            }
        }
        holder.imageGender.setImageResource(genderIcon)
        holder.tvGender.text = pair.gender

        if (pair.legband.isNullOrEmpty()) {
            holder.tvLegband.visibility = View.GONE
        } else {
            holder.tvLegband.text = pair.legband
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class PairingsViewHolder(itemView: View, private val dataList: MutableList<BirdData>) :
    RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.birdImageView)
    var tvIdentifier: TextView = itemView.findViewById(R.id.tvIdentifier)
    var tvLegband: TextView = itemView.findViewById(R.id.tvLegband)
    var tvMutation: TextView = itemView.findViewById(R.id.tvMutation)
    var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    var tvGender: TextView = itemView.findViewById(R.id.tvGender)
    var tvCage: TextView = itemView.findViewById(R.id.tvCage)
    var imageGender: ImageView = itemView.findViewById(R.id.GenderImageView)

}