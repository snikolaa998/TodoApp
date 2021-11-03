package com.example.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoDatabase
import com.example.todoapp.data.models.TodoData
import com.example.todoapp.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application, val todoRepository: TodoRepository) : AndroidViewModel(application) {

    fun insertData(todoData: TodoData) {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.insertData(todoData)
        }
    }
}