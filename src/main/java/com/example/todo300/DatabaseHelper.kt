package com.example.todo300
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ToDoDatabase"
        const val TABLE_TODO = "ToDoTable"
        const val TABLE_TASKS = "TaskTable"
        const val COLUMN_ID = "id"
        const val COLUMN_TASK = "task"
        const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_TODO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK + " TEXT,"
                + COLUMN_STATUS + " INTEGER" + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODO")
        onCreate(db)
    }
    // CRUD operations

    // Insert a new task
    fun addTask(task: String, status: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TASK, task)
        contentValues.put(COLUMN_STATUS, status)

        val success = db.insert(TABLE_TODO, null, contentValues)
        db.close()
        return success
    }

    // Get all tasks
    fun getAllTasks(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_TODO", null)
    }

    // Update a task
    fun updateTask(id: Int, task: String, status: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TASK, task)
        contentValues.put(COLUMN_STATUS, status)

        return db.update(TABLE_TODO, contentValues, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Delete a task
    fun deleteTask(id: Long): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_TODO, "$COLUMN_ID=?", arrayOf(id.toString()))
    }
}