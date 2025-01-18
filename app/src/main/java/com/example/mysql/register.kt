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

class register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: ImageButton
    private lateinit var loginButton: ImageButton

    companion object {
        private const val TAG = "RegisterActivity"
        // Hardcoded test credentials
        private const val TEST_EMAIL = "test@example.com"
        private const val TEST_PASSWORD = "password123"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize views
        emailEditText = findViewById(R.id.c)
        passwordEditText = findViewById(R.id.a)
        registerButton = findViewById(R.id.b1)
        loginButton = findViewById(R.id.b12)

        // Add test registration shortcut
        emailEditText.setOnLongClickListener {
            emailEditText.setText(TEST_EMAIL)
            passwordEditText.setText(TEST_PASSWORD)
            true
        }

        // Register button click listener
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Check for test credentials
            if (email == TEST_EMAIL && password == TEST_PASSWORD) {
                Log.d(TAG, "Test registration successful")
                Toast.makeText(this, "Test registration successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return@setOnClickListener
            }

            if (email.isEmpty() || password.isEmpty()) {
                Log.w(TAG, "Registration attempt with empty email or password")
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate password strength
            if (password.length < 6) {
                Log.w(TAG, "Password too short")
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase Authentication Registration
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        Toast.makeText(baseContext, "Registration successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}", 
                            Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Registration failed with exception", exception)
                    Toast.makeText(baseContext, "Registration error: ${exception.message}", 
                        Toast.LENGTH_LONG).show()
                }
        }

        // Login button click listener (to go back to login screen)
        loginButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
