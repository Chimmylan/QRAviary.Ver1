package com.example.qraviaryapp.activities.CagesActivity.CagesAdapter


import BirdData
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.activities.detailedactivities.BirdsDetailedActivity
import com.example.qraviaryapp.activities.detailedactivities.MoveNurseryScannerActivity
import com.example.qraviaryapp.adapter.channelId
import com.example.qraviaryapp.adapter.channelName
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class NurseryListAdapter(
    private val context: android.content.Context,
    private var dataList: MutableList<BirdData>,
    private val maturingDays: Int // Add this parameter
) :
    RecyclerView.Adapter<MyViewHolder2>() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    companion object {
        const val MAX_MUTATION_LENGTH = 10
    }
    fun getHeaderForPosition(position: Int): String {
        if (position < 0 || position >= dataList.size) {
            return ""
        }
        // Assuming dataList is sorted by mutation name
        return dataList[position].month?.substring(0, 4) ?: ""
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder2 {
        val view = LayoutInflater.from(context).inflate(R.layout.item_nurserylist, parent, false)

        return MyViewHolder2(view, dataList)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        val bird = dataList[position]

        mAuth  = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        val currentUserId = mAuth.currentUser?.uid

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


        val mutationList = listOf(
            bird.mutation1,
            bird.mutation2,
            bird.mutation3,
            bird.mutation4,
            bird.mutation5,
            bird.mutation6
        )

        val datebirth = bird.dateOfBirth
        Log.d(TAG,"${bird.maturingDays}")
        val dateFormat = SimpleDateFormat("MMM d yyyy", Locale.US)
        val birthDate = datebirth?.let { dateFormat.parse(it) }
        val currentDate = Calendar.getInstance().time

        val ageInMillis = currentDate.time - birthDate?.time!!
        val ageInDays = TimeUnit.MILLISECONDS.toDays(ageInMillis)

        if (ageInDays >= bird.maturingDays?.toInt()!!) {
            holder.layoutmovebtn.visibility = View.VISIBLE
            holder.tvGender.visibility =View.GONE
            holder.tvStatus.visibility = View.GONE
            var progressPercentage = (ageInDays.toFloat() / bird.maturingDays!!.toFloat() * 100).toInt()

            if (progressPercentage >= 100) {
                progressPercentage = 100
                sendNotification(bird, context)

                val statusRef = db.child("Users").child("ID: ${currentUserId.toString()}").child("Cages").child("Nursery Cages")
                    .child(bird.cageKey.toString())
                    .child("Birds")
                    .child(bird.adultingKey.toString())

                statusRef.child("Status").setValue("Matured")
                holder.chickImg.setImageResource(R.drawable.hatchcolor)
                holder.teenImg.setImageResource(R.drawable.chickcolor)
                holder.adultImg.setImageResource(R.drawable.adultcolor)
            }
            holder.tvpercentage.text = "$progressPercentage%"


            val animator = ObjectAnimator.ofInt(holder.tvprogressbar, "progress", progressPercentage)
            animator.duration = 1000
            animator.start()

            holder.movebtn.setOnClickListener{


                val intent = Intent(context, MoveNurseryScannerActivity::class.java)
                intent.putExtra("Nursery Key", bird.nurseryKey)
                intent.putExtra("CageKeyValue", bird.cageKey)
                intent.putExtra("BirdKey", bird.adultingKey)
                context.startActivity(intent)
            }


        } else {
            holder.layoutmovebtn.visibility = View.GONE
            holder.tvprogressbar.visibility = View.VISIBLE

            val progressPercentage = (ageInDays.toFloat() / bird.maturingDays!!.toFloat() * 100).toInt()


            holder.tvpercentage.text = "$progressPercentage%"

            val remainingDays = bird.maturingDays?.toInt()!! - ageInDays


            if (remainingDays > 0) {
                val daysText = if (remainingDays.toInt() == 1) "day" else "days"
                holder.tvTime.text = "$remainingDays $daysText left"
            }
            val animator = ObjectAnimator.ofInt(holder.tvprogressbar, "progress", progressPercentage)
            animator.duration = 1000
            animator.start()
            if (progressPercentage in 1..49) {
                holder.chickImg.setImageResource(R.drawable.hatchcolor)
            } else if (progressPercentage in 50..99) {
                holder.chickImg.setImageResource(R.drawable.hatchcolor)
                holder.teenImg.setImageResource(R.drawable.chickcolor)
            }
        }



        val nonNullMutations = mutationList.filter { !it.isNullOrBlank() }

        val combinedMutations = if (nonNullMutations.isNotEmpty()) {
            "Mutation: " + nonNullMutations.joinToString(" / ")
        } else {
            "Mutation: None"
        }

        holder.tvMutation.text = combinedMutations


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



        val genderIcon = when (bird.gender) {
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
        holder.tvGender.text = bird.gender

        if (bird.legband.isNullOrEmpty()) {
            holder.tvLegband.visibility = View.GONE
        } else {
            holder.tvLegband.text = bird.legband
        }
    }
}
    private fun sendNotification(bird: BirdData, context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        val title = "Bird is matured"
        val message = "Bird ID: ${bird.identifier} is now matured."

        val intent = Intent(context, BirdsDetailedActivity::class.java)
        // Set any data you want to pass to the detailed activity
        intent.putExtra("BirdKey", bird.birdKey)

        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logoqraviary)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(message)

        val notification = RemoteMessage.Builder("com.example.qraviaryapp")
            .setMessageId(Integer.toString(0))
            .addData("title", "Bird is matured")
            .addData("body", "Bird ID: ${bird.identifier} is now matured")
            .build()

        notificationManager?.notify(0, builder.build())
        FirebaseMessaging.getInstance().send(notification)
    }




class MyViewHolder2(itemView: View, private val dataList: MutableList<BirdData>) :
    RecyclerView.ViewHolder(itemView) {
    val tvTime: TextView = itemView.findViewById(R.id.tvdaysLeft)

    var imageView : ImageView = itemView.findViewById(R.id.birdImageView)
    var tvIdentifier: TextView = itemView.findViewById(R.id.tvIdentifier)
    var tvLegband: TextView = itemView.findViewById(R.id.tvLegband)
    var tvMutation: TextView = itemView.findViewById(R.id.tvMutation)
    var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    var tvGender: TextView = itemView.findViewById(R.id.tvGender)
    var layoutmovebtn: LinearLayout = itemView.findViewById(R.id.layoutmovebtn)
    var movebtn: Button = itemView.findViewById(R.id.movebtn)
   /* var tvCage: TextView = itemView.findViewById(R.id.tvCage)*/
    var imageGender: ImageView = itemView.findViewById(R.id.GenderImageView)
    var tvprogressbar: ProgressBar = itemView.findViewById(R.id.progressBar)
    var tvpercentage: TextView = itemView.findViewById(R.id.tvpercentage)
    var chickImg: ImageView = itemView.findViewById(R.id.chickImageView)
    var teenImg: ImageView = itemView.findViewById(R.id.teenImageView)
    var adultImg: ImageView = itemView.findViewById(R.id.adultImageView)
    init {

        itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("NurseryKey", dataList[adapterPosition].adultingKey)
            bundle.putString("BirdKey", dataList[adapterPosition].birdKey)
            bundle.putString("FlightKey", dataList[adapterPosition].flightKey)
            bundle.putString("BirdLegband", dataList[adapterPosition].legband)
            bundle.putString("BirdId", dataList[adapterPosition].identifier)
            bundle.putString("BirdImage", dataList[adapterPosition].img)
            bundle.putString("BirdGender", dataList[adapterPosition].gender)
            bundle.putString("BirdStatus", dataList[adapterPosition].status)
            bundle.putString("BirdDateBirth", dataList[adapterPosition].dateOfBirth)
            bundle.putString("BirdSalePrice", dataList[adapterPosition].soldPrice)
            bundle.putString("BirdBuyer", dataList[adapterPosition].saleContact)
            bundle.putString("BirdDeathReason", dataList[adapterPosition].deathReason)
            bundle.putString("BirdExchangeReason", dataList[adapterPosition].exReason)
            bundle.putString("BirdExchangeWith", dataList[adapterPosition].exContact)
            bundle.putString("BirdLostDetails", dataList[adapterPosition].lostDetails)
            bundle.putString("BirdAvailCage", dataList[adapterPosition].availCage)
            bundle.putString("BirdForsaleCage", dataList[adapterPosition].forSaleCage)
            bundle.putString("BirdRequestedPrice", dataList[adapterPosition].reqPrice)
            bundle.putString("BirdBuyPrice", dataList[adapterPosition].buyPrice)
            bundle.putString("BirdBoughtOn", dataList[adapterPosition].boughtDate)
            bundle.putString("BirdBoughtBreeder", dataList[adapterPosition].breederContact)
            bundle.putString("BirdMutation1", dataList[adapterPosition].mutation1)
            bundle.putString("BirdMutation2", dataList[adapterPosition].mutation2)
            bundle.putString("BirdMutation3", dataList[adapterPosition].mutation3)
            bundle.putString("BirdMutation4", dataList[adapterPosition].mutation4)
            bundle.putString("BirdMutation5", dataList[adapterPosition].mutation5)
            bundle.putString("BirdMutation6", dataList[adapterPosition].mutation6)
            bundle.putString("BirdSoldDate", dataList[adapterPosition].soldDate)
            bundle.putString("BirdDeceaseDate", dataList[adapterPosition].deathDate)
            bundle.putString("BirdExchangeDate", dataList[adapterPosition].exDate)
            bundle.putString("BirdLostDate", dataList[adapterPosition].lostDate)
            bundle.putString("BirdDonatedDate", dataList[adapterPosition].donatedDate)
            bundle.putString("BirdDonatedContact", dataList[adapterPosition].donatedContact)
            bundle.putString("BirdFather", dataList[adapterPosition].father)
            bundle.putString("BirdFatherKey", dataList[adapterPosition].fatherKey)
            bundle.putString("BirdMother", dataList[adapterPosition].mother)
            bundle.putString("BirdMotherKey", dataList[adapterPosition].motherKey)
            bundle.putString("CageKeyValue", dataList[adapterPosition].cageKey)
            bundle.putBoolean("fromNurseryListAdapter", true)
            val i = Intent(itemView.context, BirdsDetailedActivity::class.java)
            i.putExtras(bundle)
            itemView.context.startActivity(i)
        }
    }
}

