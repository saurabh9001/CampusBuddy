package com.example.mysql

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
//working
class dailyschedule : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var database: DatabaseReference
    private lateinit var connectionStatusTextView: TextView
    private lateinit var dataStatusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dailyschedule)

        recyclerView = findViewById(R.id.recyclerView)
        connectionStatusTextView = findViewById(R.id.connectionStatusTextView)
        dataStatusTextView = findViewById(R.id.dataStatusTextView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        database = FirebaseDatabase.getInstance().reference

        // Check if connected to Firebase
        checkFirebaseConnection()

        // Dynamically get the current day of the week
        val dayOfWeek = getCurrentDayOfWeek()
        Log.d("DailySchedule", "Current day of week: $dayOfWeek")

        getScheduleForDay(dayOfWeek) { scheduleList ->
            if (scheduleList.isNotEmpty()) {
                dataStatusTextView.text = "Data loaded successfully."
                scheduleAdapter = ScheduleAdapter(scheduleList)
                recyclerView.adapter = scheduleAdapter
            } else {
                dataStatusTextView.text = "No data available for $dayOfWeek"
            }
        }
    }

    private fun getCurrentDayOfWeek(): String {
        // Get the current date and time
        val currentDate = Date()
        Log.d("DailySchedule", "Current date: $currentDate")

        // Format the day of the week
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDate).lowercase(Locale.getDefault())
        Log.d("DailySchedule", "Formatted day of week: $dayOfWeek")

        return dayOfWeek
    }

    private fun checkFirebaseConnection() {
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    connectionStatusTextView.text = "Connected to Firebase"
                } else {
                    connectionStatusTextView.text = "Disconnected from Firebase"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("DailySchedule", "Listener was cancelled")
            }
        })
    }

    private fun getScheduleForDay(day: String, callback: (List<ClassSchedule>) -> Unit) {
        val dayRef = database.child("timetables").child(day)
        dayRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val scheduleList = mutableListOf<ClassSchedule>()
                snapshot.children.forEach { data ->
                    val schedule = data.getValue(ClassSchedule::class.java)
                    if (schedule != null) {
                        scheduleList.add(schedule)
                    }
                }
                callback(scheduleList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("DailySchedule", "loadPost:onCancelled", error.toException())
            }
        })
    }
}


