package com.example.qraviaryapp.activities.mainactivities

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qraviaryapp.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var etResetEmail: EditText
    private lateinit var btnReset: CardView

    private lateinit var mAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textbtn: TextView
    private lateinit var imagebtn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgot_pass)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
        )
        val abcolortitle = resources.getColor(R.color.appbar)
        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'></font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        progressBar = findViewById(R.id.btnprogressbar)
        mAuth = FirebaseAuth.getInstance()
        btnReset = findViewById(R.id.sendBtn)
        etResetEmail = findViewById(R.id.resetEmailEt)
        textbtn = findViewById(R.id.textbtn)
        imagebtn = findViewById(R.id.imageView)
        imagebtn.setOnClickListener {
               onBackPressed()
            }
        btnReset.setOnClickListener {
            showProgressBar()
            val emailValue = etResetEmail.text.toString().trim() // Trim the email to remove leading/trailing spaces

            if (emailValue.isNotEmpty()) {
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(emailValue)
                    .addOnCompleteListener { task ->
                        hideProgressBar()
                        if (task.isSuccessful) { val signInMethods = task.result?.signInMethods
                            if (signInMethods.isNullOrEmpty()) {
                                showDialogForgetPass1(
                                    "No account found. Create a new account?",
                                    "It looks like $emailValue isn't connected to an account. You can create a new account or try again",
                                    "Create", "Try Again"
                                )
                            } else {
                                mAuth.sendPasswordResetEmail(emailValue)
                                    .addOnCompleteListener { task1 ->
                                        if (task1.isSuccessful) {
                                            showDialogForgetPass(
                                                "Reset Password",
                                                "We have emailed your password reset link",
                                                "Login",
                                                "Send Again"
                                            )
                                        }
                                    }
                            }
                        }
                        else{
                            showErrorMessageDialog1(
                                "Invalid Email Address"

                            )
                        }
                    }
            } else {
                hideProgressBar()
                showErrorMessageDialog(
                    "Enter an email",
                    "You'll need to enter an email to continue"
                )
            }
        }

    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        textbtn.visibility = View.GONE
    }
    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        textbtn.visibility = View.VISIBLE
    }
    private fun showErrorMessageDialog(title: String,errorMessage: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage(errorMessage)
            .setTitle(title)
            .setPositiveButton("Try Again") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }
    private fun showErrorMessageDialog1(errorMessage: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage(errorMessage)

            .setPositiveButton("Try Again") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }
    private fun showDialogForgetPass(title: String, errorMessage: String, button: String, negbutton: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage(errorMessage)
            .setTitle(title)
            .setPositiveButton(button) { dialog, _ ->
                startActivity(Intent(this, LoginActivity::class.java))
            }
            .setNegativeButton(negbutton) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }
    private fun showDialogForgetPass1(title: String, errorMessage: String, button: String, negbutton: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage(errorMessage)
            .setTitle(title)
            .setPositiveButton(button) { dialog, _ ->
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            .setNegativeButton(negbutton) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                onBackPressed() // Call this to navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}