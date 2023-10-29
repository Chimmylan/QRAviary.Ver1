package com.example.qraviaryapp.activities.mainactivities

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.qraviaryapp.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var etResetEmail: EditText
    private lateinit var btnReset: Button

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        mAuth = FirebaseAuth.getInstance()
        btnReset = findViewById(R.id.sendBtn)
        etResetEmail = findViewById(R.id.resetEmailEt)



        btnReset.setOnClickListener {
            if (etResetEmail.text.isNotEmpty()){
                val emailValue = etResetEmail.text.toString()

                mAuth.sendPasswordResetEmail(emailValue)
                    .addOnCompleteListener{task ->
                        if (task.isSuccessful){

                            Log.d(TAG, "Reset Sent")

                        }
                    }
            }else{
                Log.d(TAG, "Input Email to Reset")
            }
        }





    }
}