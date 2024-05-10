package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class NotesActivity : AppCompatActivity() {
    private lateinit var notesET : TextInputEditText
    private lateinit var saveButton : Button
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notes)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        notesET = findViewById(R.id.notesET)
        saveButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener { saveNotes() }

        notesET.setText(sharedPreferences.getStringSet("saved", setOf())!!.joinToString("\n"))
    }

    fun saveNotes() {
        var set : Set<String> = notesET.text.toString().split("\n").toSet()
        sharedPreferences.edit().putStringSet("saved", set).apply()
        finish()
    }
}