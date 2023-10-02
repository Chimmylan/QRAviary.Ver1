package com.example.qraviaryapp.adapter

import EggData
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.EditActivities.EditEggActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

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
        val maturingDay: Int? = eggs.eggMaturingStartDate?.toInt()
        val incubatingDays = eggs.eggIncubationStartDate

        val incubatedays: Int? = incubatingDays?.toInt()



        // Check the egg status and set the date TextView visibility accordingly
        when (eggs.eggStatus) {
            "Laid" -> {
                holder.tvTime.text = eggs.eggDate
                holder.tvTime.visibility = View.VISIBLE
                holder.layoutmove.visibility = View.GONE // Hide layoutmove
                holder.layoutprogressbar.visibility = View.GONE // Show layoutmove
            }
            "Incubating" -> {
                holder.tvTime.text = eggs.eggDate
                holder.tvTime.visibility = View.VISIBLE
                holder.layoutmove.visibility = View.GONE // Show layoutmove
                holder.layoutprogressbar.visibility = View.VISIBLE // Show layoutmove

                val eggdate = eggs.eggDate
                val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.US)
                val eggDate = dateFormat.parse(eggdate)
                val currentDate = Calendar.getInstance().time

                val ageInMillis = currentDate.time - eggDate.time
                val ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMillis)
                if (ageInDays >= incubatedays!!) {
                    var progressPercentage = (ageInDays.toFloat() / incubatedays.toFloat() * 100).toInt()

                    if (progressPercentage >= 100) {
                        progressPercentage = 100
                        holder.chickImg.setImageResource(R.drawable.hatchcolor)
                        holder.eggImg.setImageResource(R.drawable.eggcolor)
                    }
                    holder.tvpercentage.text = "$progressPercentage%"

                    val animator = ObjectAnimator.ofInt(holder.progressBar, "progress", progressPercentage)
                    animator.duration = 1000
                    animator.start()
                } else {
                    holder.progressBar.visibility = View.VISIBLE

                    val progressPercentage = (ageInDays.toFloat() / incubatedays.toFloat() * 100).toInt()
                    holder.tvpercentage.text = "$progressPercentage%"
                    val animator = ObjectAnimator.ofInt(holder.progressBar, "progress", progressPercentage)
                    animator.duration = 1000
                    animator.start()
                    if (progressPercentage in 1..49) {
                        holder.eggImg.setImageResource(R.drawable.hatchcolor)
                    }
                }
            }
            "Hatched" -> {
                holder.tvTime.text = eggs.eggDate
                holder.tvTime.visibility = View.VISIBLE
                holder.layoutmove.visibility = View.VISIBLE // Show layoutmove
                holder.layoutprogressbar.visibility = View.GONE // Show layoutmove



            }
            else -> {
                // Hide the date TextView and layoutmove for other statuses
                holder.tvTime.visibility = View.GONE
                holder.layoutmove.visibility = View.GONE
                holder.layoutprogressbar.visibility = View.GONE // Show layoutmove
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
    val layoutmove: LinearLayout = itemvView.findViewById(R.id.layoutMove)
    val movebtn: Button = itemvView.findViewById(R.id.movebtn)
    val layoutprogressbar: LinearLayout = itemvView.findViewById(R.id.layoutprogressbar)
    val progressBar: ProgressBar = itemvView.findViewById(R.id.progressBar)
    val tvpercentage: TextView = itemvView.findViewById(R.id.tvpercentage)
    var chickImg: ImageView = itemView.findViewById(R.id.chickImageView)
    var eggImg: ImageView = itemView.findViewById(R.id.eggImageView)
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