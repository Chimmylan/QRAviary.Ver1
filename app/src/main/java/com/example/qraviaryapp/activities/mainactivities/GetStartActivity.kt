package com.example.qraviaryapp.activities.mainactivities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
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
            arrayOf("Opaline", 100, 21),
            arrayOf("Parblue", 100, 21),
            arrayOf("Lutino", 100, 21),
            arrayOf("Albino", 100, 21),
            arrayOf("Creamino", 100, 21),
            arrayOf("Decino", 100, 21),
            arrayOf("White Bull", 100, 21),
            arrayOf("Yellow Bull", 100, 21),
            arrayOf("Euwing", 100, 21),
            arrayOf("Greywing", 100, 21),
            arrayOf("Edge", 100, 21),
            arrayOf("Pale Fallow", 100, 21),
            arrayOf("Bronze Fallow", 100, 21),
            arrayOf("Dun Fallow", 100, 21),
            arrayOf("Dilute", 100, 21),
            arrayOf("Aqua", 100, 21),
            arrayOf("Aqua V2", 100, 21),
            arrayOf("Yellow Face", 100, 21),
            arrayOf("Red Factor", 100, 21),
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
        val intent = Intent(this@GetStartActivity, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish this activity to prevent going back to it
        snackbarMessage = "User registered successfully"
    }

    companion object {
        var snackbarMessage: String? = null
    }

}