package com.example.qraviaryapp.adapter

import BirdData
import ClickListener
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.FemaleBirdListActivity
import com.example.qraviaryapp.activities.dashboards.PairFemaleBirdListActivity

class PairFemaleBirdListAdapter(
    private val context: Context,
    private val dataList: MutableList<BirdData>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<PairFemaleViewHolder>() {

    companion object {
        private const val MAX_MUTATION_LENGTH = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairFemaleViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.item_birdlist,parent,false)

        return PairFemaleViewHolder(view, dataList, context as PairFemaleBirdListActivity)
    }



    override fun onBindViewHolder(holder: PairFemaleViewHolder, position: Int) {
        val bird = dataList[position]

        if (bird.img.isNullOrEmpty()) {
            Glide.with(context)
                .load(R.drawable.noimage)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.imageView)

        } else {

            Glide.with(context)
                .load(bird.img)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.noimage)
                .into(holder.imageView)
        }
        val maxIdentifierLength = 5 // Define your desired maximum length here
        val identifierText = if (bird.identifier?.length!! > maxIdentifierLength) {
            bird.identifier?.substring(0, maxIdentifierLength) + "..." // Add ellipsis
        } else {
            bird.identifier
        }

        holder.tvIdentifier.text = "ID: $identifierText"

        if (bird.legband.isNullOrEmpty()) {
            holder.tvLegband.visibility = View.GONE
        } else {
            holder.tvLegband.text = bird.legband
        }
        if (bird.status == "Available" || bird.status == "For Sale") {
            val cageInfo = when {
                bird.status == "Available" -> bird.availCage
                bird.status == "For Sale" -> bird.forSaleCage
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
        val mutationList = listOf(
            bird.mutation1,
            bird.mutation2,
            bird.mutation3,
            bird.mutation4,
            bird.mutation5,
            bird.mutation6
        )

        val nonNullMutations = mutationList.filter { !it.isNullOrBlank() }

        val combinedMutations = if (nonNullMutations.isNotEmpty()) {
            "Mutation: " + nonNullMutations.joinToString(" x ")
        } else {
            "Mutation: None"
        }

        holder.tvMutation.text = combinedMutations

        holder.tvStatus.text = bird.status
        val genderIcon = if(bird.gender == "Male"){
            R.drawable.baseline_male_24
        } else if (bird.gender == "Female") {
            R.drawable.female_logo
        }else{
            R.drawable.bird
        }

        holder.imageGender.setImageResource(genderIcon)
        holder.tvGender.text = bird.gender

    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class PairFemaleViewHolder(
    itemView: View,
    private val dataList: MutableList<BirdData>,
    private val activity: PairFemaleBirdListActivity
) : RecyclerView.ViewHolder(itemView) {
    var imageView : ImageView = itemView.findViewById(R.id.birdImageView)
    var tvIdentifier: TextView = itemView.findViewById(R.id.tvIdentifier)
    var tvMutation: TextView = itemView.findViewById(R.id.tvMutation)
    var tvLegband: TextView = itemView.findViewById(R.id.tvLegband)
    var tvCage: TextView = itemView.findViewById(R.id.tvCage)
    var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    var tvGender: TextView = itemView.findViewById(R.id.tvGender)
    var imageGender: ImageView = itemView.findViewById(R.id.GenderImageView)


    init {
        itemView.setOnClickListener {

            val femaleBirdKey = dataList[adapterPosition].birdKey
            val femaleFlightKey = dataList[adapterPosition].flightKey
            val mutation1 = dataList[adapterPosition].mutation1
            val mutation2 = dataList[adapterPosition].mutation2
            val mutation3 = dataList[adapterPosition].mutation3
            val mutation4 = dataList[adapterPosition].mutation4
            val mutation5 = dataList[adapterPosition].mutation5
            val mutation6 = dataList[adapterPosition].mutation6
            val femaleBirdId =
                dataList[adapterPosition].identifier // Retrieve the cage name from the data list
            val cagekey = dataList[adapterPosition].cagekeyvalue
            val cagebirdkey = dataList[adapterPosition].cagebirdkey
            val image = dataList[adapterPosition].img
            val intent = Intent()
            intent.putExtra("FemaleGallery", image)
            intent.putExtra("FemaleBirdId", femaleBirdId)
            intent.putExtra("FemaleBirdKey", femaleBirdKey)
            intent.putExtra("FemaleFlightKey", femaleFlightKey)
            intent.putExtra("FemaleBirdMutation", mutation1)
            intent.putExtra("FemaleBirdMutation2", mutation2)
            intent.putExtra("FemaleBirdMutation3", mutation3)
            intent.putExtra("FemaleBirdMutation4", mutation4)
            intent.putExtra("FemaleBirdMutation5", mutation5)
            intent.putExtra("FemaleBirdMutation6", mutation6)
            intent.putExtra("CageKeyFemale",cagekey)
            intent.putExtra("CageBirdKeyFemale",cagebirdkey)
            activity.setResult(Activity.RESULT_OK, intent)

            activity.finish()
        }

    }

}
