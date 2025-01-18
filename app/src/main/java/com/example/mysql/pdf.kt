package com.example.mysql

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class pdf : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        // Set activity title
        // Initialize Firebase
        val firebaseApp = FirebaseApp.initializeApp(this)
        if (firebaseApp == null) {
            // Firebase is not connected
            Toast.makeText(this, "Firebase is not connected", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase is connected
        Toast.makeText(this, "Firebase is connected", Toast.LENGTH_SHORT).show()

        // Access Firebase Storage
        val storage = Firebase.storage
        val storageRef = storage.reference

        // List of PDF file references and their titles
        val pdfFiles = listOf(
            "a/Resume[1].pdf" to "Question Paper 1",
            "a/Resume[1].pdf" to "Question Paper 2",
            "a/Resume[1].pdf" to "Question Paper 3",
            "a/Resume[1].pdf" to "Question Paper 4",
            "a/Resume[1].pdf" to "Question Paper 5",
            "a/Resume[1].pdf" to "Question Paper 6",
            "a/Resume[1].pdf" to "Question Paper 7",
            "a/Resume[1].pdf" to "Question Paper 8",
            "a/Resume[1].pdf" to "Question Paper 9",
            "a/Resume[1].pdf" to "Question Paper 10"
        )

        // Get the LinearLayout from layout
        val buttonContainer: LinearLayout = findViewById(R.id.buttonContainer)

        // Dynamically create buttons for each PDF file
        for ((filePath, title) in pdfFiles) {
            val button = Button(this).apply {
                text = title
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setOnClickListener {
                    openPdf(filePath)
                }
            }
            buttonContainer.addView(button)
        }
    }

    private fun openPdf(filePath: String) {
        // Reference to the PDF file
        val pdfRef = Firebase.storage.reference.child(filePath)

        pdfRef.downloadUrl.addOnSuccessListener { uri ->
            // Open the PDF using an Intent
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }.addOnFailureListener { exception ->
            // Handle any errors
            Toast.makeText(this, "Failed to load PDF: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
