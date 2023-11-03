package com.example.qraviaryapp.fragments.generateFragment

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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

    private lateinit var statusSpinner: Spinner
    private lateinit var etIncubatingDays: EditText
    private lateinit var etMaturingDays: EditText
    private lateinit var qrImage: ImageView

    private var status: String? = null


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

        OnActiveSpinner()

        generateBtn.setOnClickListener {
            val eggData = JSONObject()

            eggData.put("EggStatus", status)
            eggData.put("MaturingDays", etMaturingDays.text.toString())
            eggData.put("IncubatingDays", etIncubatingDays.text.toString())


            qrImage.setImageBitmap(generateQRCodeUri(eggData.toString()))
        }




        return view
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
//                incubatingLinearLayout.visibility = View.GONE
//                hatchedLinearLayout.visibility = View.GONE


                when (p0?.getItemAtPosition(p2).toString()) {
                    "Incubating" -> {

                        //incubatingLinearLayout.visibility = View.VISIBLE
                    }
                    "Hatched" -> {

                        //hatchedLinearLayout.visibility = View.VISIBLE
                    }

                }


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        return

    }

    companion object {
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