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
            "Opaline",
            "Parblue",
            "Lutino",
            "Albino",
            "Creamino",
            "Decino",
            "White Bull",
            "Yellow Bull",
            "Euwing",
            "Greywing",
            "Edge",
            "Pale Fallow",
            "Bronze Fallow",
            "Dun Fallow",
            "Dilute",
            "Aqua",
            "Aqua V2",
            "Yellow Face",
            "Red Factor"
        )

        for (mutation in mutations) {
            // Create a unique key for each mutation (optional, you can use your own key structure)
            val key = db.push().key

            // Store the mutation data with the generated key
            key?.let {
                val mutationData = mapOf(
                    "Mutation" to mutation
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