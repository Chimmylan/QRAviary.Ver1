package com.example.qraviaryapp.adapter

import BirdData
import ClickListener
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.dashboards.MaleBirdListActivity

class MaleBirdListAdapter
    (
    private val context: Context,
    private val dataList: MutableList<BirdData>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<MaleBirdViewHolder>() {

    companion object {
        private const val MAX_MUTATION_LENGTH = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaleBirdViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.item_birdlist,parent,false)

        return MaleBirdViewHolder(view, dataList, context as MaleBirdListActivity)
    }



    override fun onBindViewHolder(holder: MaleBirdViewHolder, position: Int) {
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


        val mutationList = mutableListOf(
            bird.mutation1,
            bird.mutation2,
            bird.mutation3,
            bird.mutation4,
            bird.mutation5,
            bird.mutation6
        )


        val nonNullMutations = mutationList.filter { !it.isNullOrBlank() }


        val combinedMutations = nonNullMutations.joinToString("\nMutation: ")


        if (combinedMutations.length > BirdListAdapter.MAX_MUTATION_LENGTH) {
            val trimmedMutations = combinedMutations.substring(0,
                BirdListAdapter.MAX_MUTATION_LENGTH
            ) + "..." // Add ellipsis
            holder.tvMutation.text = "Mutations: $trimmedMutations"
        } else {
            holder.tvMutation.text = "Mutations: $combinedMutations"
        }

        holder.tvStatus.text = bird.status
        val status = bird.status
        holder.tvStatus.text = status
        when (status) {
            "Available" -> holder.tvStatus.setTextColor(Color.parseColor("#006400"))
            "For Sale" -> holder.tvStatus.setTextColor(Color.parseColor("#000080")) // Dark blue
            "Sold" -> holder.tvStatus.setTextColor(Color.parseColor("#8B0000")) // Dark red
            "Deceased" -> holder.tvStatus.setTextColor(Color.BLACK)
            "Exchanged" -> holder.tvStatus.setTextColor(Color.CYAN) // You can change this color
            "Lost" -> holder.tvStatus.setTextColor(Color.MAGENTA)
            "Donated" -> holder.tvStatus.setTextColor(Color.YELLOW)
            "Paired" -> holder.tvStatus.setTextColor(Color.parseColor("#FF69B4"))
            else -> holder.tvStatus.setTextColor(Color.GRAY)
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

        val genderIcon = if(bird.gender == "Male"){
            R.drawable.baseline_male_24
        } else if (bird.gender == "Female") {
            R.drawable.female_logo
        }else{
            R.drawable.bird
        }

        holder.imageGender.setImageResource(genderIcon)
        holder.tvGender.text = bird.gender

        if (bird.legband.isNullOrEmpty()) {
            holder.tvLegband.visibility = View.GONE
        } else {
            holder.tvLegband.text = bird.legband
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class MaleBirdViewHolder(
    itemView: View,
    private val dataList: MutableList<BirdData>,
    private val activity: MaleBirdListActivity
) : RecyclerView.ViewHolder(itemView) {
    var imageView : ImageView = itemView.findViewById(R.id.birdImageView)
    var tvIdentifier: TextView = itemView.findViewById(R.id.tvIdentifier)
    var tvMutation: TextView = itemView.findViewById(R.id.tvMutation)
    var tvLegband: TextView = itemView.findViewById(R.id.tvLegband)
    var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    var tvGender: TextView = itemView.findViewById(R.id.tvGender)
    var tvCage: TextView = itemView.findViewById(R.id.tvCage)
    var imageGender: ImageView = itemView.findViewById(R.id.GenderImageView)


    init {
        itemView.setOnClickListener {

            val maleBirdKey = dataList[adapterPosition].birdKey
            val maleFlightKey = dataList[adapterPosition].flightKey
            val maleBirdMutation = dataList[adapterPosition].mutation1
            val maleBird =
                dataList[adapterPosition].identifier // Retrieve the cage name from the data list

            val intent = Intent()
            intent.putExtra("MaleBirdId", maleBird)
            intent.putExtra("MaleBirdKey", maleBirdKey)
            intent.putExtra("MaleFlightKey", maleFlightKey)
            intent.putExtra("MaleMutation", maleBirdMutation)
            activity.setResult(Activity.RESULT_OK, intent)

            activity.finish()
        }

    }

}