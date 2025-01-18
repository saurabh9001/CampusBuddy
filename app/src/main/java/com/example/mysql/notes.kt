package com.example.mysql


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class notes : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewNotes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter { noteId -> deleteNote(noteId) }
        recyclerView.adapter = adapter

        val btnSave: Button = findViewById(R.id.btnSave)
        btnSave.setOnClickListener {
            saveNote()
        }

        val btnRefresh: Button = findViewById(R.id.btnRefresh)
        btnRefresh.setOnClickListener {
            loadNotes()
        }

        // Load notes from Firestore
        loadNotes()
    }

    private fun saveNote() {
        val etTitle: EditText = findViewById(R.id.etTitle)
        val etDescription: EditText = findViewById(R.id.etDescription)
        val etDate: EditText = findViewById(R.id.etDate)
        val etTime: EditText = findViewById(R.id.etTime)
        val etAmPm: EditText = findViewById(R.id.etAmPm)

        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val date = etDate.text.toString().trim()
        val time = etTime.text.toString().trim()
        val amPm = etAmPm.text.toString().trim()

        if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && amPm.isNotEmpty()) {
            val dateTime = "$date $time $amPm"
            val note = NotesAdapter.Note(
                title = title,
                description = description,
                dateTime = dateTime
            )

            db.collection("notes")
                .add(note)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
                    etTitle.setText("")
                    etDescription.setText("")
                    etDate.setText("")
                    etTime.setText("")
                    etAmPm.setText("")
                    loadNotes()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving note: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadNotes() {
        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                val notesList = mutableListOf<NotesAdapter.Note>()
                for (document in result) {
                    val note = document.toObject(NotesAdapter.Note::class.java).apply {
                        id = document.id
                    }
                    notesList.add(note)
                }
                adapter.setNotes(notesList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error loading notes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteNote(noteId: String) {
        db.collection("notes").document(noteId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Note deleted successfully", Toast.LENGTH_SHORT).show()
                loadNotes()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error deleting note: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
