package com.example.qraviaryapp.adapter

import android.content.Context
import android.widget.TextView
import com.example.qraviaryapp.R
import com.github.mikephil.charting.components.MarkerView

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MyMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val textView: TextView = findViewById(R.id.markerTextView)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e != null) {
            // Format the timestamp as "MMM dd yyyy" before displaying it
            val dateInMillis = e.x.toLong()
            val formattedDate = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                .format(Date(dateInMillis))
            val decimalFormat = DecimalFormat("#,###.##") // Define your format here
            val formattedValue = decimalFormat.format(e.y)

            textView.text = "Date: $formattedDate\nValue: $formattedValue"
        }
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }
}
