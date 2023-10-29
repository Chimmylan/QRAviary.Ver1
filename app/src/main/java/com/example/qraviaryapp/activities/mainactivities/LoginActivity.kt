package com.example.qraviaryapp.activities.mainactivities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.*

import com.example.qraviaryapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var googleprogressbar: ProgressBar
   // private lateinit var FrameLayout: FrameLayout
    private lateinit var textbtn: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var lemail: TextInputEditText
    private lateinit var lpass: TextInputEditText
    private lateinit var layoutpass: TextInputLayout
    private lateinit var layoutemail: TextInputLayout
    private lateinit var rememberme: CheckBox
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbase: DatabaseReference
//    private var KEY_EMAIL: String = "email"
//    private var KEY_REMEMBER_ME: String = "remember00User"
//    private var KEY_IS_LOGGED_IN: String = "isLoggedIn"


    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: CardView

    companion object {
        private const val RC_SIGN_IN = 1000
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = Firebase.auth
        layoutemail= findViewById(R.id.layoutemail)
        layoutpass= findViewById(R.id.layoutpass)
        lemail = findViewById(R.id.etLogEmail)
        lpass = findViewById(R.id.etLogPass)
//        rememberme = findViewById(R.id.cbrem)
        progressBar = findViewById(R.id.btnprogressbar)
        googleprogressbar = findViewById(R.id.btnprogressbar1)
       // FrameLayout = findViewById(R.id.etLogPass)
        textbtn = findViewById(R.id.textbtn)

        val reg = findViewById<CardView>(R.id.tvRegisterHere)
        val forgot = findViewById<TextView>(R.id.tvforgot)


        mAuth = FirebaseAuth.getInstance()
        //val currentUserId = mAuth.currentUser?.uid

        dbase = FirebaseDatabase.getInstance().reference
        sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val isRemembered = sharedPreferences.getBoolean("rememberUser", false)

        if (GetStartActivity.snackbarMessage != null) {
            showSnackbar(GetStartActivity.snackbarMessage!!)
            // Reset the message to prevent showing it again
            GetStartActivity.snackbarMessage = null
        }
        if (isRemembered) {
            startActivity(Intent(this@LoginActivity, NavHomeActivity::class.java))
            finish()
        }
//        rememberme.isChecked = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)


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
            // Show the progress bar
            showGoogleProgressBar()
            signIn()

        }

        forgot.setOnClickListener { forgot() }


    }
    private fun signIn() {

        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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

        // Start the animation
        shakeAnimation.start()
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
            hideGoogleProgressBar()
            if (task.isSuccessful) {
                val currentUser = mAuth.currentUser
                val uid = currentUser?.uid ?: ""
                val email = currentUser?.email ?: ""

                val userReference = dbase.child("Users")

                userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.hasChild("ID: $uid")) {
                            Log.d(TAG, "Main Page")
                            startActivity(Intent(this@LoginActivity, NavHomeActivity::class.java))
                            finish()
                        } else {
                            Log.d(TAG, "Get Started Page")
                            startActivity(Intent(this@LoginActivity, GetStart1Activity::class.java))
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
        val intent = Intent(this@LoginActivity, NavHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun isOnline(): Boolean {
        val connectivtyManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivtyManager.activeNetwork
        val activeNetwork = connectivtyManager.getNetworkCapabilities(network)
        if (activeNetwork != null) {
            return true
        }
        return false
    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        textbtn.visibility = View.GONE
    }
    private fun showGoogleProgressBar(){
        googleprogressbar.visibility = View.VISIBLE
    }
    private fun hideGoogleProgressBar() {
        googleprogressbar.visibility = View.INVISIBLE

    }
    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        textbtn.visibility = View.VISIBLE
    }
    private var loginAttempts = 0
//    fun login(view: View) {
//        val email = lemail.text.toString()
//        val password = lpass.text.toString()
//
//
//        showProgressBar()
//
//
//        if (!isOnline()) {
//            // Show message to connect to the internet
//
//            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && !isOnline()) {
//                layoutemail.helperText = "Email cannot be empty"
//                layoutpass.helperText = "Password cannot be empty"
//                vibrateAnimation(layoutemail)
//                vibrateAnimation(layoutpass)
//
//                Toast.makeText(
//                    this,
//                    "Please connect to the internet and try again later",
//                    Toast.LENGTH_LONG
//                ).show()
//                hideProgressBar()
//                return
//
//            }
//            else if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !isOnline()) {
//                Toast.makeText(
//                    this,
//                    "Please connect to the internet and try again later",
//                    Toast.LENGTH_LONG
//                ).show()
//                hideProgressBar()
//                return
//            }
//            else if (TextUtils.isEmpty(email) && !isOnline()) {
//                layoutemail.helperText = "Email cannot be empty"
//                vibrateAnimation(layoutemail)
//
//                Toast.makeText(
//                    this,
//                    "Please connect to the internet and try again later",
//                    Toast.LENGTH_LONG
//                ).show()
//                hideProgressBar()
//                return
//            }
//            else if (!TextUtils.isEmpty(email) && !isOnline()) {
//
//                Toast.makeText(
//                    this,
//                    "Please connect to the internet and try again later",
//                    Toast.LENGTH_LONG
//                ).show()
//                hideProgressBar()
//                return
//            }
//            else if (TextUtils.isEmpty(password) && !isOnline()) {
//                layoutpass.helperText = "Password cannot be empty"
//                vibrateAnimation(layoutpass)
//                Toast.makeText(
//                    this,
//                    "Please connect to the internet and try again later",
//                    Toast.LENGTH_LONG
//                ).show()
//                hideProgressBar()
//                return
//            }
//            else if (!TextUtils.isEmpty(password) && !isOnline()) {
//                Toast.makeText(
//                    this,
//                    "Please connect to the internet and try again later",
//                    Toast.LENGTH_LONG
//                ).show()
//                hideProgressBar()
//                return
//            }
////            Toast.makeText(
////                this,
////                "Please connect to the internet and try again later",
////                Toast.LENGTH_LONG
////            ).show()
////
////            return
//        }
//        else if (isOnline()) {
//            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && isOnline()) {
//                layoutemail.helperText = "Email cannot be empty"
//                layoutpass.helperText = "Password cannot be empty"
//                vibrateAnimation(layoutemail)
//                vibrateAnimation(layoutpass)
//                hideProgressBar()
//                return
//            } else if (TextUtils.isEmpty(email) && isOnline()) {
//                layoutemail.helperText = "Email cannot be empty"
//                vibrateAnimation(layoutemail)
//                hideProgressBar()
//                return
//            } else if (TextUtils.isEmpty(password) && isOnline()) {
//                layoutpass.helperText = "Password cannot be empty"
//                vibrateAnimation(layoutpass)
//                hideProgressBar()
//                return
//            }
//            // Check for internet connection
//            else if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && isOnline()) {
//                mAuth.signInWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this) { task ->
//                        hideProgressBar()
//                        if (task.isSuccessful) {
//                            if(mAuth.currentUser?.isEmailVerified == true){
//
//                                startActivity(Intent(this@LoginActivity, NavHomeActivity::class.java))
//                                finish()
//
//
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "User logged in successfully",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                            else{
//                                showSnackBar("Email Not Verified",
//                                    "Please verify your email now. You cannot login without email verification",
//                                    "Continue")
//                            }
//
//
//
//                        } else {
//
//                            val exception = task.exception
//                            if (exception is FirebaseAuthException) {
//                                val errorCode = exception.errorCode
//                                when (errorCode) {
//                                    "ERROR_INVALID_EMAIL", "ERROR_WRONG_PASSWORD" -> {
//                                        // Show the error message in an AlertDialog
//                                        showErrorMessageDialog("Invalid email or password")
//                                        hideProgressBar()
//                                    }
//
//                                    else -> {
//                                        /* layoutemail.helperText = "Invalid Email"*/
//                                        /* layoutpass.helperText = "Invalid Password"*/
//                                        showDialogForgetPass("Did you forget your password?",
//                                            "Continue to change your password",
//                                            "Continue", "Try Again")
//                                        hideProgressBar()
//                                    }
//                                }
//                            } else {
//                                Toast.makeText(
//                                    this@LoginActivity,
//                                    "Error occurred while logging in",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//
//            }
//        }
//
//    }

    fun login(view: View) {
        val email = lemail.text.toString()
        val password = lpass.text.toString()


        if (TextUtils.isEmpty(email)) {
            showSnackBar("Email required", "Enter your email to continue", "OK")
            vibrateAnimation(layoutemail)
            return
        }

        if (TextUtils.isEmpty(password)) {
            showSnackBar("Password required", "Enter your email to continue", "OK")
            vibrateAnimation(layoutpass)
            return
        }
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password) ){
            showSnackBar("Email required", "Enter your email to continue", "OK")
            vibrateAnimation(layoutpass)
            vibrateAnimation(layoutemail)
            return
        }

        if (!isOnline()) {
            Toast.makeText(this, "Please connect to the internet and try again later", Toast.LENGTH_LONG).show()
            return
        }

        showProgressBar()
        lpass.isEnabled = false
        lemail.isEnabled = false
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                hideProgressBar()
                lpass.isEnabled = true
                lemail.isEnabled = true
                if (task.isSuccessful) {
                    if (mAuth.currentUser?.isEmailVerified == true) {

                        startActivity(Intent(this@LoginActivity, NavHomeActivity::class.java))
                        finish()
                        Toast.makeText(this@LoginActivity, "User logged in successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        showSnackBar(
                            "Email Not Verified",
                            "Please verify your email now. You cannot login without email verification",
                            "Continue"
                        )
                    }
                } else {
                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val signInMethods = task.result?.signInMethods
                                if (signInMethods.isNullOrEmpty()) {
                                    showDialogForgetPass(
                                        "No account found. Create a new account?",
                                        "It looks like $email isn't connected to an account. You can create a new account or try again",
                                        "Create",
                                        "Try Again"
                                    )
                                } else {
                                    showDialogForgetPass(
                                        "Did you forget your password?",
                                        "We can help you log into your account if you've forgotten your password.",
                                        "Forgot Password",
                                        "Try Again"
                                    )
                                }
                            }else {
                                // Handle the task failure (e.g., show an error dialog)
                                val exception = task.exception
                                if (exception is FirebaseAuthException) {
                                    // Handle specific errors
                                    when (exception.errorCode) {
                                        "ERROR_INVALID_EMAIL", "ERROR_WRONG_PASSWORD" -> {
                                            showErrorMessageDialog(
                                                "Invalid email or password.")

                                        }

                                        else -> {
                                            // Handle other errors
                                        }
                                    }
                                }
                            }
                        }
                }
            }
    }

    private fun showSnackbar(message: String) {
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

    private fun showErrorMessageDialog(errorMessage: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage(errorMessage)
            .setPositiveButton("Try Again") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }
    private fun showSnackBar(title: String, errorMessage: String, button: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setMessage(errorMessage)
            .setTitle(title)
            .setPositiveButton(button) { dialog, _ ->
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
                startActivity(Intent(this@LoginActivity, ForgotPassActivity::class.java))
            }
            .setNegativeButton(negbutton) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()

        alertDialog.show()
    }

    fun forgot() {
        startActivity(Intent(this@LoginActivity, ForgotPassActivity::class.java))
    }
    fun reg(view: View) {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }
}