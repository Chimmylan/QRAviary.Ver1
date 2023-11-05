package com.example.qraviaryapp.activities.detailedactivities


import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.qraviaryapp.R
import com.example.qraviaryapp.fragments.generateFragment.GenerateEggFragment
import com.google.android.material.button.MaterialButton
import java.io.IOException

class QRCodeActivity : AppCompatActivity() {
    private lateinit var qrcode: ImageView
    private lateinit var qrimageLayout: LinearLayout
    private val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 1 // You can use any integer value you prefer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }
        setContentView(R.layout.activity_qrcode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.toolbarcolor
                )
            )
        )
        qrimageLayout = findViewById(R.id.qrimagelayout)
        val abcolortitle = resources.getColor(R.color.appbar)

        supportActionBar?.title = HtmlCompat.fromHtml(
            "<font color='$abcolortitle'>QR code</font>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        // Check if night mode is enabled
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_white)
        qrcode = findViewById(R.id.qrcode)
        val qr = intent?.getStringExtra("CageQR")

        val qrCodeUrl = qr

        Glide.with(this)
            .load(qrCodeUrl)
            .into(qrcode)

//        val cardView = findViewById<CardView>(R.id.cardView)

        val saveButton = findViewById<MaterialButton>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Check for permission to write to external storage



        }
        saveButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted. You can proceed with saving the image.
                saveImage()
            } else {
                // Request the WRITE_EXTERNAL_STORAGE permission.
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    GenerateEggFragment.WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                )
            }
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GenerateEggFragment.WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now save the image.
                    saveImage()
                } else {
                    // Permission denied, show a message or handle it accordingly.
                    Toast.makeText(this, "Permission denied. Image cannot be saved.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun captureLayoutAsBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false // Release the cache
        return bitmap
    }
    fun saveImage() {
        val layoutBitmap = captureLayoutAsBitmap(qrimageLayout)

        save(layoutBitmap)
    }

    fun save(bitmap: Bitmap?) {
        val displayName = "image.jpg"
        val mimeType = "image/jpeg"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val resolver = this.contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (imageUri != null) {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }

            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "Error: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
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