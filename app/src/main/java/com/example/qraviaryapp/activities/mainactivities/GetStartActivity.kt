package com.example.qraviaryapp.activities.mainactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import com.example.qraviaryapp.R
import com.example.qraviaryapp.R.id.progressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GetStartActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_start)
        val progressBar = findViewById<ProgressBar>(progressBar)
        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference
        showProgressBar(progressBar)
        Handler().postDelayed({
            insertDataToFirebase()
            hideProgressBar(progressBar)
            navigateToHomeActivity()
        }, 1000) // 1000 milliseconds = 1 second
    }

    private fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.INVISIBLE
    }

    private fun insertDataToFirebase() {
        val currentUserId = mAuth.currentUser?.uid
        val db = FirebaseDatabase.getInstance().reference.child("Users")
            .child("ID: ${currentUserId.toString()}").child("Mutations")
        val mutations = arrayOf(
            arrayOf("Opaline", 30, 14),
            arrayOf("Parblue", 20, 23),
            arrayOf("Lutino", 15, 12),
            arrayOf("Albino", 32, 18),
            arrayOf("Creamino", 35, 19),
            arrayOf("Decino", 64, 10),
            arrayOf("White Bull", 21, 29),
            arrayOf("Yellow Bull", 43, 23),
            arrayOf("Euwing", 9, 20),
            arrayOf("Greywing", 15, 24),
            arrayOf("Edge", 24, 29),
            arrayOf("Pale Fallow", 19, 43),
            arrayOf("Bronze Fallow", 29, 28),
            arrayOf("Dun Fallow", 18, 30),
            arrayOf("Dilute", 28, 22),
            arrayOf("Aqua", 43, 21),
            arrayOf("Aqua V2", 20, 32),
            arrayOf("Yellow Face", 34, 38),
            arrayOf("Red Factor", 14, 35),
        )

        for (mutation in mutations) {
            // Create a unique key for each mutation (optional, you can use your own key structure)
            val key = db.push().key


            val mutationName = mutation[0]
            val mutationMaturingDay = mutation[1]
            val mutationIncubatingDay = mutation[2];


            // Store the mutation data with the generated key
            key?.let {
                val mutationData = mapOf(
                    "Mutation" to mutationName,
                    "Maturing Days" to mutationMaturingDay,
                    "Incubating Days" to mutationIncubatingDay
                    // Add more fields if needed
                )

                // Set the data in Firebase
                db.child(key).setValue(mutationData)
            }
        }

    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this@GetStartActivity, NavHomeActivity::class.java)
        startActivity(intent)
        finish() // Finish this activity to prevent going back to it
    }
}