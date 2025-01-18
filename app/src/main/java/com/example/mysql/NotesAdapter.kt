package com.example.mysql
//////working
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val deleteNote: (String) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var notesList: List<Note> = ArrayList()

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val dateTimeTextView: TextView = itemView.findViewById(R.id.dateTimeTextView)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentItem = notesList[position]
        holder.titleTextView.text = currentItem.title
        holder.descriptionTextView.text = currentItem.description
        holder.dateTimeTextView.text = currentItem.dateTime
        holder.btnDelete.setOnClickListener {
            deleteNote.invoke(currentItem.id)
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    fun setNotes(notes: List<Note>) {
        notesList = notes
        notifyDataSetChanged()
    }
    data class Note(
        var id: String = "",
        val title: String = "",
        val description: String = "",
        val dateTime: String = ""
    )

}
