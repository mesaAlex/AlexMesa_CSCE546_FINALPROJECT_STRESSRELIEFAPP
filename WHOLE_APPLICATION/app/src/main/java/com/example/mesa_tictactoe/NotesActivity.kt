package com.example.mesa_tictactoe

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

// This code is used to make the notes that everyone adds to the app look a little nicer
class NoteAdapter(private val context: android.content.Context, private val notes: List<Pair<String, String>>) :
    ArrayAdapter<Pair<String, String>>(context, 0, notes) {

    override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
        val view = convertView ?: android.view.LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)

        val noteTitle = view.findViewById<TextView>(R.id.noteTitle)
        val noteBody = view.findViewById<TextView>(R.id.noteBody)

        val note = notes[position]
        noteTitle.text = note.first
        noteBody.text = note.second

        return view
    }
}

class NotesActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var bodyEditText: EditText
    private lateinit var addNoteButton: Button
    private lateinit var backButton: Button
    private lateinit var notesListView: ListView
    private lateinit var adapter: NoteAdapter

    private val notesList = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        titleEditText = findViewById(R.id.titleEditText)
        bodyEditText = findViewById(R.id.bodyEditText)
        addNoteButton = findViewById(R.id.addNoteButton)
        backButton = findViewById(R.id.backButton)
        notesListView = findViewById(R.id.notesListView)

        adapter = NoteAdapter(this, notesList)
        notesListView.adapter = adapter

        addNoteButton.setOnClickListener {
            addNote()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun addNote() {
        val title = titleEditText.text.toString()
        val body = bodyEditText.text.toString()

        if (title.isNotEmpty() && body.isNotEmpty()) {
            notesList.add(Pair(title, body))
            adapter.notifyDataSetChanged()

            // Fade animation
            val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
            notesListView.startAnimation(animation)

            titleEditText.text.clear()
            bodyEditText.text.clear()
        } else {
            Toast.makeText(this, "Please fill in both fields!", Toast.LENGTH_SHORT).show()
        }
    }
}