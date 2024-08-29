package com.example.todo300

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo300.databinding.ActivityMainBinding
import android.app.AlertDialog
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        dbHelper = DatabaseHelper(this)

        // Initialize RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        updateTaskList()

        binding.fab.setOnClickListener {
            val taskEditText = EditText(this)
            taskEditText.hint = "Enter task"

            AlertDialog.Builder(this)
                .setTitle("Create Note")
                .setMessage("Enter your task:")
                .setView(taskEditText)
                .setPositiveButton("Create") { dialog, _ ->
                    val task = taskEditText.text.toString()
                    if (task.isNotEmpty()) {
                        val taskId = dbHelper.addTask(task, 0)
                        if (taskId != -1L) {
                            Toast.makeText(this, "Task created successfully!", Toast.LENGTH_SHORT).show()
                            updateTaskList()  // Update the list after adding a new task
                        } else {
                            Toast.makeText(this, "Error creating task.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun updateTaskList() {
        val tasks = mutableListOf<Note>()
        val cursor = dbHelper.getAllTasks()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val task = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TASK))
            val status = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS))
            tasks.add(Note(id, task, status))
        }
        cursor.close()

        adapter = NoteAdapter(tasks, dbHelper) { updateTaskList() }
        binding.recyclerView.adapter = adapter
    }
}
