package com.example.todo300

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private val notes: List<Note>,
    private val dbHelper: DatabaseHelper,
    private val onNoteModified: () -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTextView: TextView = itemView.findViewById(R.id.taskTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.taskTextView.text = note.task

        // Handle the Edit button click
        holder.editButton.setOnClickListener {
            // Create an EditText to enter the updated task
            val editText = EditText(holder.itemView.context)
            editText.setText(note.task) // Pre-fill with current task

            // Show an AlertDialog to edit the task
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Edit Task")
                .setMessage("Update your task:")
                .setView(editText)
                .setPositiveButton("Update") { dialog, _ ->
                    val updatedTask = editText.text.toString()
                    if (updatedTask.isNotEmpty()) {
                        // Update the task in the database
                        dbHelper.updateTask(note.id.toInt(), updatedTask, note.status)
                        Toast.makeText(holder.itemView.context, "Task updated!", Toast.LENGTH_SHORT).show()

                        // Refresh the list
                        onNoteModified()
                    } else {
                        Toast.makeText(holder.itemView.context, "Task cannot be empty!", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        holder.deleteButton.setOnClickListener {
            // Handle delete action
            dbHelper.deleteTask(note.id)
            onNoteModified()
        }
    }

    override fun getItemCount(): Int = notes.size
}
