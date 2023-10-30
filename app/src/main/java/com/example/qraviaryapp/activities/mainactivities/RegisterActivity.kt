package com.example.qraviaryapp.activities.mainactivities

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.qraviaryapp.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbase: DatabaseReference
    private lateinit var rusername: TextInputEditText
    private lateinit var remail: TextInputEditText
    private lateinit var rpass: TextInputEditText
    private lateinit var cpass: TextInputEditText
    private lateinit var layoutname: TextInputLayout
    private lateinit var layoutconpass: TextInputLayout
    private lateinit var layoutpass: TextInputLayout
    private lateinit var layoutemail: TextInputLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var googleprogressbar: ProgressBar
    private lateinit var textbtn: TextView
    private lateinit var textgooglebtn: LinearLayout
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: CardView
    companion object {
        private const val RC_SIGN_IN = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        layoutname = findViewById(R.id.layoutname)
        layoutpass= findViewById(R.id.layoutpass)
        layoutconpass= findViewById(R.id.layoutconpass)
        layoutemail= findViewById(R.id.layoutemail)
        remail = findViewById(R.id.etRegEmail)
        rusername = findViewById(R.id.etRegUsername)
        rpass = findViewById(R.id.etRegPass)
        cpass = findViewById(R.id.etConPass)
        progressBar = findViewById(R.id.btnprogressbar)
        googleprogressbar = findViewById(R.id.btnprogressbar1)
        textbtn = findViewById(R.id.textbtn)
        textgooglebtn= findViewById(R.id.txtgooglebtn)
        dbase = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()

        val login = findViewById<CardView>(R.id.tvLoginHere)



        remail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used in this case
            }
        })
        rusername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateUsername(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used in this case
            }
        })
        rpass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used in this case
            }
        })
        cpass.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateConfirmPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used in this case
            }
        })

        googleBtn = findViewById(R.id.google_btn)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            navigateToSecondActivity()
        }
        googleBtn.setOnClickListener {
            showGoogleProgressBar()

            Handler().postDelayed({
                hideGoogleProgressBar()
                signIn()
            }, 3000)
        }


    }
    private fun showGoogleProgressBar() {
        googleprogressbar.visibility = View.VISIBLE
        textgooglebtn.visibility = View.GONE
    }

    private fun hideGoogleProgressBar() {
        googleprogressbar.visibility = View.INVISIBLE
        textgooglebtn.visibility =  View.VISIBLE
    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        textbtn.visibility = View.GONE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        textbtn.visibility = View.VISIBLE
    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    firebaseAuthWithGoogle(it)
                }
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK && data != null) {
                handleSignInResult(data) // Call the handleSignInResult() method here with the data Intent
            } else {
                // Sign-in failed or was canceled
                // Handle the error or take appropriate action
                Log.d(TAG, "Sign-in failed or was canceled")
            }
        }
    }
    private fun handleSignInResult(data: Intent) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            // Get the signed-in account
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                // The user is authenticated, and the email address is associated with a Google account
                val email = account.email

                Log.d(ContentValues.TAG,"Authenticated")
                // Now you can check if the email address already exists in your backend or database
                // Proceed with your app's logic accordingly
            } else {
                // Account is null, handle sign-in failure
                // Show an error message or take appropriate action
                Log.d(ContentValues.TAG,"Not Authenticated")
            }
        } catch (e: ApiException) {
            // Handle sign-in failure (e.g., user canceled the sign-in)
            // Show an error message or take appropriate action
        }
    }
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val currentUser = mAuth.currentUser
                val uid = currentUser?.uid ?: ""
                val email = currentUser?.email ?: ""

                val userReference = dbase.child("Users")

                userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChild("ID: $uid")) {
                            Log.d(TAG, "Main Page")
                            startActivity(Intent(this@RegisterActivity, NavHomeActivity::class.java))
                            finish()
                        } else {
                            Log.d(TAG, "Get Started Page")
                            startActivity(Intent(this@RegisterActivity, GetStart1Activity::class.java))
                            finish()

                            // Since the user does not exist, create a new entry in the database
                            val userData = hashMapOf("Name" to uid)
                            userReference.child("ID: $uid").setValue(userData)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error as needed.
                        Log.e(TAG, "Error occurred: ${databaseError.message}")
                    }
                })

                // Sign-in success, navigate to the second activity
                //navigateToSecondActivity()
            } else {
                // Sign-in failed
                Toast.makeText(applicationContext, "Google sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToSecondActivity() {
        val intent = Intent(this@RegisterActivity, NavHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateEmail(email: String) {
        if (!TextUtils.isEmpty(email)) {
            val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
            if (!emailRegex.matches(email)) {
                layoutemail.error = "Invalid email input"
            } else {
                layoutemail.helperText = "Valid email input"
                layoutemail.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#306844")))
                layoutemail.error = null
            }
        } else {
            layoutemail.error = "Email cannot be empty"
        }
    }
    private fun validateUsername(username: String) {
        if (TextUtils.isEmpty(username)) {
            layoutname.helperText = "Username cannot be empty"
            layoutname.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
        }
        else if (!TextUtils.isEmpty(username)) {
            layoutname.helperText = ""

        }
        if (username.length > 10) {
            layoutname.helperText = "Username cannot be more than 10"
            layoutname.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
        }

    }
    private fun validatePassword(password: String) {
        val confirmPassword = cpass.text.toString()
        if (!TextUtils.isEmpty(password)) {
            if (password != confirmPassword){
                layoutconpass.helperText ="Password does not match"
                layoutconpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
            }
            else {
                // layoutconpass.error = null
                layoutconpass.helperText = "Password Match!"
                layoutconpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#306844")))
            }
            // Add your password validation logic here
            // Example: check if the password meets the required criteria
            if (password.length >= 10) {
                layoutpass.helperText = "Strong Password"
                layoutpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#306844")))
            } else if (password.length < 5) {
                layoutpass.error = "Too weak"
            } else if (password.length in 5..9) {
                layoutpass.helperText = "Average"
                layoutpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#FFA500")))
            } else {
                layoutpass.error = null
            }
        } else {
            layoutpass.error = "Password cannot be empty"
            layoutpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
        }
    }
    private fun validateConfirmPassword(confirmPassword: String) {
        val password = rpass.text.toString()
        if (!TextUtils.isEmpty(confirmPassword)) {
            if (password != confirmPassword) {
                layoutconpass.helperText = "Password does not match"
                //layoutconpass.helperText = null
                layoutconpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
            } else {
                // layoutconpass.error = null
                layoutconpass.helperText = "Password Match!"
                layoutconpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#306844")))
            }
        } else {
            layoutconpass.error = "Confirm Password cannot be empty"
            layoutconpass.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
            layoutconpass.helperText = null
        }
    }

    fun reg(view: View) {
        val username = rusername.text.toString()
        val email = remail.text.toString()
        val password = rpass.text.toString()
        val conpassword = cpass.text.toString()
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")

        var validEmail = false
        var validPass = false
        var validConfiPass = false
        var validUsername = false


        showProgressBar()



            if (TextUtils.isEmpty(email)) {
                layoutemail.helperText = "Email cannot be empty"
            } else if (!emailRegex.matches(email)) {
                layoutemail.helperText = "Invalid email input"
            } else {
                validEmail = true
            }
            if (TextUtils.isEmpty(username)) {
                layoutname.helperText = "Username cannot be empty"
            }
            else {
                validEmail = true
            }
            if (TextUtils.isEmpty(password)) {
                layoutpass.helperText = "Password cannot be empty"
            } else {
                validPass = true
            }

            if (TextUtils.isEmpty(conpassword)) {
                layoutconpass.helperText = "Password cannot be empty"
            }
            else if (password != conpassword) {
                layoutconpass.helperText = "Password does not match"
            } else {
                validConfiPass = true
            }

            val valid = validEmail && validConfiPass && validPass

            if (valid) {
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        hideProgressBar()
                        if (task.isSuccessful) {
                            val userId = mAuth.currentUser!!.uid

                            // Send email verification link
                            mAuth.currentUser!!.sendEmailVerification()
                                .addOnCompleteListener { verificationTask ->
                                    hideProgressBar()
                                    if (verificationTask.isSuccessful) {

                                        showVerifiedMessage("Verification email sent, please check your inbox")


                                        val myRef = dbase.child("Users").child("ID: $userId")

                                        val userReference = dbase.child("Users")
                                        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                if (dataSnapshot.hasChild("ID: $userId")) {
                                                    Log.d(TAG, "Main Page")
                                                    // TODO: User already exists, navigate to the main page.
                                                } else {
                                                    Log.d(TAG, "Get Started Page")
                                                    // TODO: User does not exist, navigate to the Get Started page.

                                                    // Since the user does not exist, create a new entry in the database
                                                    val userData = hashMapOf(
                                                        "UserID" to userId,
                                                        "Email" to email,
                                                        "Username" to username // Add the username to the userData HashMap
                                                    )
                                                    myRef.setValue(userData).addOnSuccessListener {
                                                        Toast.makeText(this@RegisterActivity, "User registered successfully", Toast.LENGTH_LONG).show()

                                                        //TODO Make the user go to the Get Started Page
                                                        startActivity(Intent(this@RegisterActivity, GetStartActivity::class.java))
                                                        finish()

                                                    }.addOnFailureListener {

                                                    }
                                                    userReference.child("ID: $userId").setValue(userData)
                                                }
                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                // Handle the error as needed.
                                                Log.e(TAG, "Error occurred: ${databaseError.message}")
                                            }
                                        })
                                    } else {
                                        showSuccessSnackbar("Success")
                                    }
                                }
                        } else {
                            showErrorSnackbar("Error")
                            hideProgressBar()

                            val errorCode = (task.exception as FirebaseAuthException).errorCode
                            if (errorCode == "ERROR_INVALID_EMAIL") {
                                showErrorSnackbar("User registration failed")
                                layoutemail.helperText = "Invalid Email"
                                layoutemail.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
                                vibrateAnimation(layoutemail)
                            } else if (errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                                showErrorSnackbar("User registration failed")
                                layoutemail.helperText = "There is an existing account associated with this email"
                                layoutemail.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
                                vibrateAnimation(layoutemail)
                            } else if (errorCode == "ERROR_WEAK_PASSWORD") {
                                layoutpass.helperText = "Password should be at least 8 characters"
                                layoutemail.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#5A0808")))
                                vibrateAnimation(layoutpass)
                            }
                        }
                    }
            } else {

                showProgressBar()



                    if (!validEmail) {
                        hideProgressBar()
                        vibrateAnimation(layoutemail)
                    }
                    if (!validPass) {
                        hideProgressBar()
                        vibrateAnimation(layoutpass)
                    }
                    if (!validConfiPass) {
                        hideProgressBar()
                        vibrateAnimation(layoutconpass)
                    }


        }
    }

    private fun showSuccessSnackbar(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
    private fun showVerifiedMessage(message: String) {
        val coordinatorLayout = findViewById<View>(R.id.coordinatorLayout)
        val marginInDp = 40 // Define the margin in dp

        Snackbar.make(
            coordinatorLayout, // Use the CoordinatorLayout as the parent view
            message,
            Snackbar.LENGTH_SHORT
        ).also { snackbar ->
            val snackbarView = snackbar.view

            snackbarView.setBackgroundColor(Color.parseColor("#800080"))
            val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams

            // Set gravity to top
            params.gravity = Gravity.TOP

            // Set top margin in dp
            params.topMargin = (marginInDp * resources.displayMetrics.density).toInt()

            snackbarView.layoutParams = params
        }.show()
    }
    private fun showErrorSnackbar(message: String) {
        val coordinatorLayout = findViewById<View>(R.id.coordinatorLayout)
        val marginInDp = 40 // Define the margin in dp

        Snackbar.make(
            coordinatorLayout, // Use the CoordinatorLayout as the parent view
            message,
            Snackbar.LENGTH_SHORT
        ).also { snackbar ->
            val snackbarView = snackbar.view

            snackbarView.setBackgroundColor(Color.RED)
            val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams

            // Set gravity to top
            params.gravity = Gravity.TOP

            // Set top margin in dp
            params.topMargin = (marginInDp * resources.displayMetrics.density).toInt()

            snackbarView.layoutParams = params
        }.show()
    }
    private fun vibrateAnimation(view: View) {
        val shakeAnimation = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("translationX", 0f, -10f, 10f, -10f, 10f, 0f),
            PropertyValuesHolder.ofFloat("translationY", 0f, -10f, 10f, -10f, 10f, 0f)
        ).apply {
            duration = 300 // Duration of the animation in milliseconds
            interpolator = AccelerateDecelerateInterpolator() // Optional: adjust the animation's interpolation
            repeatCount = 1 // Number of times the animation should repeat
        }


        shakeAnimation.start()
    }


    fun login(view: View) {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }


}
