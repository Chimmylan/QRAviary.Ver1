package com.example.qraviaryapp.adapter

import EggData
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Looper
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
import com.example.qraviaryapp.activities.CagesActivity.MoveNurseryActivity
import com.example.qraviaryapp.activities.EditActivities.EditEggActivity
import com.example.qraviaryapp.activities.detailedactivities.BirdsDetailedActivity
import com.example.qraviaryapp.activities.detailedactivities.ClutchesDetailedActivity
import com.example.qraviaryapp.activities.detailedactivities.MoveEggActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class EggClutchesListAdapter(
    private val context: Context,
    private val dataList: MutableList<EggData>,

) : RecyclerView.Adapter<EggClutchesHolder>() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference

    private var currentUserId: String? = null
    private var status: String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EggClutchesHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_eggclutch, parent, false)

        return EggClutchesHolder(view, dataList)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EggClutchesHolder, position: Int) {
        val eggs = dataList[position]

        holder.tvStatus.text = eggs.eggStatus
        holder.tvDate.text = eggs.eggDate
        val maturingDay: Int? = eggs.eggMaturingStartDate?.toInt()
        val incubatingDays = eggs.eggIncubationStartDate

        val incubatedays: Int? = incubatingDays?.toInt()

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        currentUserId = mAuth.currentUser?.uid

        // Check the egg status and set the date TextView visibility accordingly
        when (eggs.eggStatus) {
            "Laid" -> {
                holder.tvTime.text = eggs.eggDate
                holder.tvTime.visibility = View.VISIBLE
                holder.layoutmove.visibility = View.GONE // Hide layoutmove
                holder.layoutprogressbar.visibility = View.GONE // Show layoutmove
            }
            "Incubating" -> {

                holder.tvTime.visibility = View.VISIBLE
                holder.layoutmove.visibility = View.GONE // Show layoutmove
                holder.layoutprogressbar.visibility = View.VISIBLE // Show layoutmove

                val eggdate = eggs.eggDate
                val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.US)
                val eggDate = dateFormat.parse(eggdate)
                val currentDate = Calendar.getInstance().time

                val formattedDate = dateFormat.format(currentDate)
                val ageInMillis = currentDate.time - eggDate.time
                val ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMillis)
                if (ageInDays >= incubatedays!!) {
                    var progressPercentage = (ageInDays.toFloat() / incubatedays.toFloat() * 100).toInt()

                    if (progressPercentage >= 100) {
                        progressPercentage = 100
                        val eggRef =
                            db.child("Users").child("ID: $currentUserId").child("Pairs").child(eggs.pairKey.toString())
                                .child("Clutches")
                                .child(eggs.eggKey.toString()).child(eggs.individualEggKey.toString())

                        eggRef.child("Status").setValue("Hatched")
                        eggRef.child("Date").setValue(formattedDate)
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
                    if (progressPercentage in 1..99) {
                        holder.eggImg.setImageResource(R.drawable.hatchcolor)
                    }

                    val remainingDays = incubatedays - ageInDays


                    if (remainingDays > 0) {
                        val daysText = if (remainingDays.toInt() == 1) "day" else "days"
                        holder.tvTime.text = "$remainingDays $daysText left"
                    }
                }

            }
            "Hatched" -> {
//                holder.tvTime.text = "ready to move in nursery cage!"
                holder.tvTime.visibility = View.GONE
                holder.layoutmove.visibility = View.VISIBLE // Show layoutmove
                holder.layoutprogressbar.visibility = View.GONE // Show layoutmove




                val eggdate = eggs.eggDate
                val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.US)
                val eggDate = dateFormat.parse(eggdate)
                val currentDate = Calendar.getInstance().time

                val ageInMillis = currentDate.time - eggDate.time
                val ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMillis).toInt() // Convert to Int

                // Check if the date is the current date
                if (ageInDays == 0) {
                    holder.tvDate.text = "TODAY"
                } else {
                    holder.tvDate.text = eggs.eggDate
                }



            }
            "Moved" -> {
                holder.tvTime.text = "ID"
                holder.tvTime.visibility = View.GONE
                holder.layoutmove.visibility = View.GONE // Show layoutmove
                holder.layoutprogressbar.visibility = View.GONE // Show layoutmove



            }
            else -> {
                // Hide the date TextView and layoutmove for other statuses
                holder.tvTime.visibility = View.GONE
                holder.layoutmove.visibility = View.GONE
                holder.layoutprogressbar.visibility = View.GONE // Show layoutmove
            }
        }

        holder.movebtn.setOnClickListener{
            val intent = Intent(context, MoveEggActivity::class.java)
            intent.putExtra("IncubatingStartDate", eggs.eggIncubationStartDate)
            intent.putExtra("MaturingStartDate", eggs.eggMaturingStartDate)
            intent.putExtra("EggKey", eggs.eggKey)
            intent.putExtra("IndividualEggKey", eggs.individualEggKey)
            intent.putExtra("PairKey", eggs.pairKey)
            intent.putExtra("PairFlightMaleKey", eggs.pairFlightMaleKey)
            intent.putExtra("PairFlightFemaleKey", eggs.pairFlightFemaleKey)
            intent.putExtra("PairMaleKey", eggs.pairBirdMaleKey)
            intent.putExtra("PairFemaleKey", eggs.pairBirdFemaleKey)
            intent.putExtra("PairMaleID", eggs.pairMaleId)
            intent.putExtra("PairFemaleID", eggs.pairFemaleId)
            intent.putExtra("DateOfBirth", eggs.eggDate)
            intent.putExtra("CageKeyFemale",eggs.eggcagekeyFemale)
            intent.putExtra("CageKeyMale",eggs.eggcagekeyMale)
            intent.putExtra("CageBirdFemale",eggs.eggcagebirdFemale)
            intent.putExtra("CageBirdMale",eggs.eggcagebirdMale)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateDataSet(newData: MutableList<EggData>){
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
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
    var tvpercentage: TextView = itemvView.findViewById(R.id.tvpercentage)
    var chickImg: ImageView = itemView.findViewById(R.id.chickImageView)
    var eggImg: ImageView = itemView.findViewById(R.id.eggImageView)
    init {



            itemView.setOnClickListener {
                if (dataList[adapterPosition].eggStatus == "Moved") {
                    val i = Intent(itemvView.context, BirdsDetailedActivity::class.java)
                    itemvView.context.startActivity(i)
                }else {
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
}