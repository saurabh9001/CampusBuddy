package com.example.mysql

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: ImageButton
    private lateinit var registerButton: ImageButton

    companion object {
        private const val TAG = "MainActivity"
        // Hardcoded test credentials
        private const val TEST_EMAIL = "test@example.com"
        private const val TEST_PASSWORD = "password123"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Check if user is already logged in
        if (auth.currentUser != null) {
            // User is signed in, go to home activity
            Log.d(TAG, "User already logged in, redirecting to home")
            startActivity(Intent(this, home::class.java))
            finish()
            return
        }

        // Initialize views
        emailEditText = findViewById(R.id.c)
        passwordEditText = findViewById(R.id.a)
        loginButton = findViewById(R.id.b12)
        registerButton = findViewById(R.id.b1)

        // Add test login shortcut
        emailEditText.setOnLongClickListener {
            emailEditText.setText(TEST_EMAIL)
            passwordEditText.setText(TEST_PASSWORD)
            true
        }

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Check for test credentials
            if (email == TEST_EMAIL && password == TEST_PASSWORD) {
                Log.d(TAG, "Test login successful")
                startActivity(Intent(this, home::class.java))
                finish()
                return@setOnClickListener
            }

            if (email.isEmpty() || password.isEmpty()) {
                Log.w(TAG, "Login attempt with empty email or password")
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase Authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(baseContext, "Login successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, home::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", 
                            Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Login failed with exception", exception)
                    Toast.makeText(baseContext, "Login error: ${exception.message}", 
                        Toast.LENGTH_LONG).show()
                }
        }

        // Register button click listener
        registerButton.setOnClickListener {
            startActivity(Intent(this, register::class.java))
        }
    }
}
