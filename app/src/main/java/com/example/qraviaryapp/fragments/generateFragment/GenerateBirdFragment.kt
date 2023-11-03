package com.example.qraviaryapp.fragments.generateFragment

import BirdData
import BirdDataListener
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import com.example.qraviaryapp.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GenerateBirdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GenerateBirdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var datePickerDialogBirth: DatePickerDialog
    /*  private lateinit var datePickerDialogBanding: DatePickerDialog*/
    private lateinit var datePickerDialogSoldDate: DatePickerDialog
    private lateinit var datePickerDialogDeathDate: DatePickerDialog
    private lateinit var datePickerDialogExDate: DatePickerDialog
    private lateinit var datepickerDialogLostDate: DatePickerDialog
    private lateinit var datepickerDialogDonatedDate: DatePickerDialog

    /*ImageView*/
    private lateinit var imageView: ImageView

    /*RadioGroup*/
    private lateinit var rgGender: RadioGroup

    /*RadioGroup*/
    private lateinit var rbFemale: RadioButton
    private lateinit var rbMale: RadioButton
    private lateinit var rbUnknown: RadioButton

    /*Spinner*/
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnMutation1: MaterialButton
    private lateinit var btnMutation2: MaterialButton
    private lateinit var btnMutation3: MaterialButton
    private lateinit var btnMutation4: MaterialButton
    private lateinit var btnMutation5: MaterialButton
    private lateinit var btnMutation6: MaterialButton

    /*Button*/
    private lateinit var btnLostDate: MaterialButton
    private lateinit var btnDeathDate: MaterialButton
    private lateinit var btnSoldSaleDate: Button
    private lateinit var btnExDate: MaterialButton
    private lateinit var datebandButton: MaterialButton
    private lateinit var datebirthButton: MaterialButton
    private lateinit var btnDonatedDate: MaterialButton

    private lateinit var addBtn: Button
    private lateinit var removeBtn: Button


    /*Edit Text*/
    private lateinit var etExWith: EditText
    private lateinit var etDonateChooseContract: EditText
    private lateinit var etSoldBuyer: EditText
    private lateinit var etOtherComm: EditText
    private lateinit var etExReason: EditText
    private lateinit var etDeathReason: EditText
    private lateinit var etSoldSalePrice: EditText
    private lateinit var etAvailCage: MaterialButton
    private lateinit var etForSaleCage: MaterialButton
    private lateinit var etForSaleReqPrice: EditText
    lateinit var etIdentifier: EditText
    private lateinit var etLostDetails: EditText
    private lateinit var LayoutLegband: LinearLayout
    private lateinit var layoutIdentifier: LinearLayout
    private lateinit var tvLegband: TextView
    private lateinit var tvIdentifier: TextView
    private lateinit var etLegband: EditText
    /*Layouts*/
    private lateinit var editTextContainer: LinearLayout
    private lateinit var availableLayout: LinearLayout
    private lateinit var forSaleLayout: LinearLayout
    private lateinit var soldLayout: LinearLayout
    private lateinit var deceasedLayout: LinearLayout
    private lateinit var exchangeLayout: LinearLayout
    private lateinit var lostLayout: LinearLayout
    private lateinit var donatedLayout: LinearLayout
    private lateinit var otherLayout: LinearLayout
    private var birdDataListener: BirdDataListener? = null
    private var bandFormattedDate: String? = null
    private var birthFormattedDate: String? = null
    private var soldFormattedDate: String? = null
    private var deathFormattedDate: String? = null
    private var exchangeFormattedDate: String? = null
    private var lostFormattedDate: String? = null
    private var donatedFormattedDate: String? = null

    private var mutation1IncubatingDays: String? = null
    private var mutation1MaturingDays: String? = null
    private var mutation2IncubatingDays: String? = null
    private var mutation2MaturingDays: String? = null
    private var mutation3IncubatingDays: String? = null
    private var mutation3MaturingDays: String? = null
    private var mutation4IncubatingDays: String? = null
    private var mutation4MaturingDays: String? = null
    private var mutation5IncubatingDays: String? = null
    private var mutation5MaturingDays: String? = null
    private var mutation6IncubatingDays: String? = null
    private var mutation6MaturingDays: String? = null
        private var status: String? = null
    private lateinit var cageReference: DatabaseReference
    //endregion
    private lateinit var sharedPreferences: SharedPreferences

    var birdData = BirdData()
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
       val view = inflater.inflate(R.layout.fragment_generate_bird, container, false)

        datebirthButton = view.findViewById(R.id.btndatebirth)
        btnSoldSaleDate = view.findViewById(R.id.soldSaleDate)
        btnDonatedDate = view.findViewById(R.id.btnDonatedDate)
        btnLostDate = view.findViewById(R.id.lostDateBtn)
        btnDeathDate = view.findViewById(R.id.deathDate)
        btnExDate = view.findViewById(R.id.exDate)
        initDatePickers()
        showDatePickerDialog(requireContext(), datebirthButton, datePickerDialogBirth)
        /*showDatePickerDialog(requireContext(), datebandButton, datePickerDialogBanding)*/
        showDatePickerDialog(requireContext(), btnDeathDate, datePickerDialogDeathDate)
        showDatePickerDialog(requireContext(), btnSoldSaleDate, datePickerDialogSoldDate)
        showDatePickerDialog(requireContext(), btnExDate, datePickerDialogExDate)
        showDatePickerDialog(requireContext(), btnLostDate, datepickerDialogLostDate)
        showDatePickerDialog(requireContext(), btnDonatedDate, datepickerDialogDonatedDate)


        LayoutLegband = view.findViewById(R.id.layoutlegband)
        layoutIdentifier = view.findViewById(R.id.layoutIdentifier)
        tvLegband = view.findViewById(R.id.tvLegband)
        tvIdentifier = view.findViewById(R.id.tvidentifier)
        editTextContainer = view.findViewById(R.id.editTextContainer)
        availableLayout = view.findViewById(R.id.availableLayout)
        forSaleLayout = view.findViewById(R.id.forSaleLayout)
        soldLayout = view.findViewById(R.id.soldLayout)
        deceasedLayout = view.findViewById(R.id.deceasedLayout)
        exchangeLayout = view.findViewById(R.id.exchangeLayout)
        lostLayout = view.findViewById(R.id.lostLayout)
        donatedLayout = view.findViewById(R.id.donatedLayout)
        otherLayout = view.findViewById(R.id.otherLayout)
        /*========+++++++++++++==============*/
        /*ImageView*/
        /*RadioGroup*/
        rgGender = view.findViewById(R.id.radioGroupGender)
        /*RadioButtons*/
        rbMale = view.findViewById(R.id.radioButtonMale)
        rbFemale = view.findViewById(R.id.radioButtonFemale)
        rbUnknown = view.findViewById(R.id.radioButtonUnknown)


        /*EditText*/
        etLegband = view.findViewById(R.id.etLegband)
        etAvailCage = view.findViewById(R.id.etAvailCage)
        etForSaleCage = view.findViewById(R.id.etForSaleCage)
        etForSaleReqPrice = view.findViewById(R.id.etForSaleReqPrice)
        etSoldSalePrice = view.findViewById(R.id.soldSalePrice)
        etOtherComm = view.findViewById(R.id.etOthersComm)
        etExReason = view.findViewById(R.id.exReason)
        etDeathReason = view.findViewById(R.id.deathReason)
        etIdentifier = view.findViewById(R.id.etIdentifier)
        etLostDetails = view.findViewById(R.id.etLostDetails)
        etSoldBuyer = view.findViewById(R.id.etSaleContact)
        etDonateChooseContract = view.findViewById(R.id.etDonateChooseContact)
        etExWith = view.findViewById(R.id.exchangeWithBtn)

        /*Buttons*/
        addBtn = view.findViewById(R.id.addBtn)
        removeBtn = view.findViewById(R.id.removeBtn)


        /*Spinners*/
        btnMutation1 = view.findViewById(R.id.mutationBtn1)
        btnMutation2 = view.findViewById(R.id.mutationBtn2)
        btnMutation3 = view.findViewById(R.id.mutationBtn3)
        btnMutation4 = view.findViewById(R.id.mutationBtn4)
        btnMutation5 = view.findViewById(R.id.mutationBtn5)
        btnMutation6 = view.findViewById(R.id.mutationBtn6)
        spinnerStatus = view.findViewById(R.id.spinnerstatus)
        return view
    }
    private fun initDatePickers() {
        val dateSetListenerBirth =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                birthFormattedDate = makeDateString(day, month + 1, year)
                datebirthButton.text = birthFormattedDate
            }

        /*val dateSetListenerBanding =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                bandFormattedDate = makeDateString(day, month + 1, year)
                datebandButton.text = bandFormattedDate
            }*/
        val dateSetListenerSoldDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                soldFormattedDate = makeDateString(day, month + 1, year)

                btnSoldSaleDate.text = soldFormattedDate
            }
        val dateSetListenerDeathDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                deathFormattedDate = makeDateString(day, month + 1, year)
                btnDeathDate.text = deathFormattedDate
            }
        val dateSetListenerExchangeDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                exchangeFormattedDate = makeDateString(day, month + 1, year)
                btnExDate.text = exchangeFormattedDate
            }
        val dateSetListenerLostDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                lostFormattedDate = makeDateString(day, month + 1, year)
                btnLostDate.text = lostFormattedDate
            }
        val dateSetListenerDonatedDate =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                donatedFormattedDate = makeDateString(day, month + 1, year)
                btnDonatedDate.text = donatedFormattedDate
            }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialogBirth =
            DatePickerDialog(requireContext(), style, dateSetListenerBirth, year, month, day)
        /* datePickerDialogBanding =
             DatePickerDialog(requireContext(), style, dateSetListenerBanding, year, month, day)*/
        datePickerDialogSoldDate =
            DatePickerDialog(requireContext(), style, dateSetListenerSoldDate, year, month, day)
        datePickerDialogDeathDate =
            DatePickerDialog(requireContext(), style, dateSetListenerDeathDate, year, month, day)
        datePickerDialogExDate =
            DatePickerDialog(requireContext(), style, dateSetListenerExchangeDate, year, month, day)
        datepickerDialogLostDate =
            DatePickerDialog(requireContext(), style, dateSetListenerLostDate, year, month, day)
        datepickerDialogDonatedDate =
            DatePickerDialog(requireContext(), style, dateSetListenerDonatedDate, year, month, day)

        // You can set the max date for each dialog if needed.
        // For example:
        // datePickerDialogBirth.datePicker.maxDate = System.currentTimeMillis()
        // datePickerDialogBanding.datePicker.maxDate = System.currentTimeMillis()
    }

    fun showDatePickerDialog(context: Context, button: Button, datePickerDialog: DatePickerDialog) {
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GenerateBirdFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GenerateBirdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}