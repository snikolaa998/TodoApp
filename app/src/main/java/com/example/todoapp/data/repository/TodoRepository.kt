package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.TodoDao
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.data.models.TodoData

class TodoRepository(val db: TodoDatabase) {
    suspend fun insertData(todoData: TodoData) {
        db.toDoDao().insertData(todoData)
    }

    fun getAllData() = db.toDoDao().getAll()
}