package com.example.mysql

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class time : AppCompatActivity() {

    private lateinit var textView: TextView
    private var targetTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        textView = findViewById(R.id.textView)

        // Set target time manually using a time picker dialog
        showTimePickerDialog()

        // Check current time periodically
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val currentTime = getCurrentTime()
                targetTime?.let {
                    if (currentTime == it) {
                        displayMessage("Your message here")
                    }
                }
                handler.postDelayed(this, 1000) // Check every second
            }
        }
        handler.post(runnable)
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val cal = Calendar.getInstance()
        return dateFormat.format(cal.time)
    }

    private fun showTimePickerDialog() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                targetTime = String.format("%02d:%02d", hourOfDay, minute)
            },
            hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun displayMessage(message: String) {
        textView.text = message
    }
}
