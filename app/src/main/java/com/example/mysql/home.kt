package com.example.mysql

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class home : AppCompatActivity() {

    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var linearLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var scheduleAdapter: ScheduleAdapter
    private lateinit var database: DatabaseReference
    private var startX = 0f

    @SuppressLint("CutPasteId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize UI Components
        horizontalScrollView = findViewById(R.id.horizontalScrollView)
        linearLayout = findViewById(R.id.linearLayout)
        recyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView Layout Manager with horizontal orientation
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Initialize Firebase Database Reference
        database = FirebaseDatabase.getInstance().reference

        // Check Firebase Connection
        checkFirebaseConnection()

        // Get the current day of the week
        val dayOfWeek = getCurrentDayOfWeek()

        // Load Schedule for the current day
        loadSchedule(dayOfWeek)

        // Set up UI Interactions
        setupUIInteractions()
    }

    private fun getCurrentDayOfWeek(): String {
        val currentDate = Date()
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(currentDate).lowercase(Locale.getDefault())
    }

    private fun checkFirebaseConnection() {
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    // connectionStatusTextView.text = "Connected to Firebase"
                } else {
                    // connectionStatusTextView.text = "Disconnected from Firebase"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("DailySchedule", "Listener was cancelled")
            }
        })
    }

    private fun loadSchedule(day: String) {
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
                if (scheduleList.isNotEmpty()) {
                    // dataStatusTextView.text = "Data loaded successfully."
                    scheduleAdapter = ScheduleAdapter(scheduleList)
                    recyclerView.adapter = scheduleAdapter
                } else {
                    // dataStatusTextView.text = "No data available for $day"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("DailySchedule", "loadPost:onCancelled", error.toException())
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUIInteractions() {
        horizontalScrollView.viewTreeObserver.addOnScrollChangedListener {
            applyEffects(horizontalScrollView, linearLayout)
        }

        horizontalScrollView.viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    scrollToButton(6)
                    applyEffects(horizontalScrollView, linearLayout)
                    horizontalScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        )

        horizontalScrollView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> startX = motionEvent.x
                MotionEvent.ACTION_UP -> {
                    val endX = motionEvent.x
                    val dx = endX - startX
                    if (dx > 0) {
                        smoothScrollToLeft()
                    } else if (dx < 0) {
                        smoothScrollToRight()
                    } else {
                        snapToCenter()
                    }
                }
            }
            false
        }

        setButtonClickListeners()
    }

    private fun applyEffects(scrollView: HorizontalScrollView, container: LinearLayout) {
        val centerX = scrollView.width / 2
        val threshold = 300
        var closestButton: ImageButton? = null
        var minDistance = Int.MAX_VALUE

        for (i in 0 until container.childCount) {
            val button = container.getChildAt(i) as ImageButton
            val buttonLocation = IntArray(2)
            button.getLocationOnScreen(buttonLocation)

            val buttonCenterX = buttonLocation[0] + button.width / 2
            val distanceToCenter = abs(centerX - buttonCenterX)

            if (distanceToCenter < minDistance) {
                minDistance = distanceToCenter
                closestButton = button
            }

            val scale = if (distanceToCenter < threshold) {
                1.5f - (0.5f * distanceToCenter / threshold)
            } else {
                1.0f
            }

            val alpha = if (distanceToCenter < threshold) {
                1.0f - (0.5f * distanceToCenter / threshold)
            } else {
                0.5f
            }

            button.animate()
                .scaleX(scale)
                .scaleY(scale)
                .alpha(alpha)
                .setInterpolator(android.view.animation.AccelerateDecelerateInterpolator())
                .setDuration(100)
                .start()
        }
    }

    private fun snapToCenter() {
        val centerX = horizontalScrollView.width / 2
        var closestView: ImageButton? = null
        var minDistance = Int.MAX_VALUE

        for (i in 0 until linearLayout.childCount) {
            val button = linearLayout.getChildAt(i) as ImageButton
            val buttonLocation = IntArray(2)
            button.getLocationOnScreen(buttonLocation)

            val buttonCenterX = buttonLocation[0] + button.width / 2
            val distanceToCenter = abs(centerX - buttonCenterX)

            if (distanceToCenter < minDistance) {
                minDistance = distanceToCenter
                closestView = button
            }
        }

        closestView?.let {
            val scrollX = it.left - (horizontalScrollView.width / 2 - it.width / 2)
            horizontalScrollView.smoothScrollTo(scrollX, 0)
        }
    }

    private fun scrollToButton(index: Int) {
        if (index < 0 || index >= linearLayout.childCount) return

        val button = linearLayout.getChildAt(index) as ImageButton
        val scrollX = button.left - (horizontalScrollView.width / 2 - button.width / 2)
        horizontalScrollView.smoothScrollTo(scrollX, 0)
    }

    private fun smoothScrollToLeft() {
        horizontalScrollView.smoothScrollBy(-horizontalScrollView.width / 3, 0)
    }

    private fun smoothScrollToRight() {
        horizontalScrollView.smoothScrollBy(horizontalScrollView.width / 3, 0)
    }

    private fun setButtonClickListeners() {
        val buttonClick = findViewById<ImageButton>(R.id.button5)
        buttonClick.setOnClickListener {
            val intent = Intent(this@home, calculator::class.java)
            startActivity(intent)
        }

        val buttonClick1 = findViewById<ImageButton>(R.id.button3)
        buttonClick1.setOnClickListener {
            val intent = Intent(this@home, pdf::class.java)
            startActivity(intent)
        }

        val buttonClick2 = findViewById<ImageButton>(R.id.button6)
        buttonClick2.setOnClickListener {
            val intent = Intent(this@home, notes::class.java)
            startActivity(intent)
        }

        val buttonClick3 = findViewById<ImageButton>(R.id.button7)
        buttonClick3.setOnClickListener {
            val intent = Intent(this@home, dailyschedule::class.java)
            startActivity(intent)
        }

        val btnLogout = findViewById<ImageButton>(R.id.b2)
        btnLogout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            val intent = Intent(this@home, MainActivity::class.java)
            overridePendingTransition(R.anim.slide_up_down, R.anim.slide_up_down)
            startActivity(intent)
            finish()
        }
    }
}
