package com.example.qraviaryapp.fragments.generateFragment

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateEggFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateEggFragment : Fragment() {



    private lateinit var hatchedDatePickerDialog: DatePickerDialog
    private lateinit var incubatingDatePickerDialog: DatePickerDialog
    private lateinit var btnHatched: MaterialButton
    private lateinit var btnIncubating: MaterialButton

    private var hatchedFormattedDate: String? = null
    private var incubatingFormattedDate: String? = null


    private lateinit var statusSpinner: Spinner
    private lateinit var etIncubatingDays: EditText
    private lateinit var etMaturingDays: EditText
    private lateinit var qrImage: ImageView
    private lateinit var btndownload: MaterialButton
    private lateinit var qrimageLayout: LinearLayout

    private lateinit var statusTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var incubatingDaysTv: TextView
    private lateinit var maturingDaysTv: TextView
    private lateinit var hatchedDateBtn : MaterialButton
    private lateinit var incubatingDateBtn : MaterialButton


    private lateinit var hatchedLinearLayout: LinearLayout
    private lateinit var incubatingLinearLayout: LinearLayout

    private var status: String? = null
    private val eggData = JSONObject()

    private lateinit var generateBtn: MaterialButton

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_generate_egg, container, false)

        statusSpinner = view.findViewById(R.id.eggspinnerstatus)
        etIncubatingDays = view.findViewById(R.id.etincubationdays)
        etMaturingDays = view.findViewById(R.id.etmaturingdays)
        generateBtn = view.findViewById(R.id.generate)
        qrImage = view.findViewById(R.id.QRimage)
        btndownload = view.findViewById(R.id.btndownload)
        qrimageLayout = view.findViewById(R.id.qrimagelayout)
        statusTv = view.findViewById(R.id.eggStatus)
        dateTv = view.findViewById(R.id.eggDate)
        incubatingDaysTv = view.findViewById(R.id.eggIncubatingDays)
        maturingDaysTv = view.findViewById(R.id.eggMaturingDays)
        incubatingLinearLayout = view.findViewById(R.id.incubationDateLayout)
        hatchedLinearLayout = view.findViewById(R.id.hatchedDateLayout)
        btnHatched = view.findViewById(R.id.btn_hatchedstartdate)
        btnIncubating = view.findViewById(R.id.btn_incubatingstartdate)
        OnActiveSpinner()
        initDatePickers()


        showDatePickerDialog(requireContext(), btnHatched, hatchedDatePickerDialog)
        showDatePickerDialog(requireContext(), btnIncubating, incubatingDatePickerDialog)



        generateBtn.setOnClickListener {

            qrimageLayout.visibility = View.VISIBLE
            var currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US)

            val formattedDate = currentDate.format(formatter)

            if (btnHatched.text.toString() == "TODAY") {
                // Set the incubating date to the current date and time


                btnHatched.text = formattedDate
            }
            if (btnIncubating.text.toString() == "TODAY") {
                // Set the incubating date to the current date and time

                btnIncubating.text = formattedDate
            }
            eggData.put("AddEggQR", true)
            eggData.put("EggStatus", status)

            if (hatchedLinearLayout.visibility == View.VISIBLE){
                eggData.put("EggDate", btnHatched.text)
                dateTv.text = "Date: ${btnHatched.text}"

            }else if(incubatingLinearLayout.visibility == View.VISIBLE){
                eggData.put("EggDate", btnIncubating.text)
                dateTv.text = "Date: ${btnIncubating.text}"

            }else{
                eggData.put("EggDate", currentDate)
                dateTv.text = "Date: $currentDate"
            }


            eggData.put("MaturingDays", etMaturingDays.text.toString())
            eggData.put("IncubatingDays", etIncubatingDays.text.toString())

            statusTv.text = "Status: $status"
            maturingDaysTv.text = "Maturing Days: ${etMaturingDays.text}"
            incubatingDaysTv.text = "Incubating Days: ${etIncubatingDays.text}"



            qrImage.setImageBitmap(generateQRCodeUri(eggData.toString()))
        }
        btndownload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission is already granted. You can proceed with saving the image.
                saveImage()
            } else {
                // Request the WRITE_EXTERNAL_STORAGE permission.
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            }
        }




        return view
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, you can now save the image.
                    saveImage()
                } else {
                    // Permission denied, show a message or handle it accordingly.
                    Toast.makeText(requireContext(), "Permission denied. Image cannot be saved.", Toast.LENGTH_SHORT).show()
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


//    fun saveImage(){
//        qrimageLayout.isDrawingCacheEnabled = true
//        qrimageLayout.buildDrawingCache()
//        qrimageLayout.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
//        val bitmap: Bitmap? = qrimageLayout.drawingCache
//
//        save(bitmap)
//    }
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

        val resolver = requireActivity().contentResolver
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (imageUri != null) {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }

            Toast.makeText(requireContext(), "Image saved to gallery", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(requireContext(), "Error: ${e.toString()}", Toast.LENGTH_SHORT).show()
        }
    }





    private fun generateQRCodeUri(bundleEggData: String): Bitmap? {
        val multiFormatWriter = MultiFormatWriter()
        val bitMatrix = multiFormatWriter.encode(bundleEggData, BarcodeFormat.QR_CODE, 400, 400)
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.createBitmap(bitMatrix)

        // Create a file to store the QR code image
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile("QRCode", ".png", storageDir)

        try {
            val stream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // Convert the file URI to a string and return
        return bitmap
    }

    fun OnActiveSpinner() {
        val spinnerItems = resources.getStringArray(R.array.EggStatus)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = adapter

        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val selectedItem = p0?.getItemAtPosition(p2).toString()
                status = selectedItem
                incubatingLinearLayout.visibility = View.GONE
                hatchedLinearLayout.visibility = View.GONE


                when (p0?.getItemAtPosition(p2).toString()) {
                    "Incubating" -> {

                        incubatingLinearLayout.visibility = View.VISIBLE
                    }
                    "Hatched" -> {

                        hatchedLinearLayout.visibility = View.VISIBLE
                    }

                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        return

    }

    private fun initDatePickers() {
        val dateSetListenerIncubating =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                incubatingFormattedDate = makeDateString(day, month + 1, year)
                btnIncubating.text = incubatingFormattedDate
            }
        val dateSetListenerHatched =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                hatchedFormattedDate = makeDateString(day, month + 1, year)
                btnHatched.text = hatchedFormattedDate
            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        incubatingDatePickerDialog =
            DatePickerDialog(requireContext(), style, dateSetListenerIncubating, year, month, day)
        hatchedDatePickerDialog =
            DatePickerDialog(requireContext(), style, dateSetListenerHatched, year, month, day)

    }

    fun showDatePickerDialog(
        context: Context, button: Button, datePickerDialog: DatePickerDialog
    ) {
        button.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JUL"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN" // Default should never happen
        }
    }


    companion object {
        const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1 // You can choose any integer value
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GenerateEggFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateEggFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}