package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.todoapp.viewmodels.TodoViewModel
import com.example.todoapp.viewmodels.TodoViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(findNavController(R.id.navHostFragment))

        val viewModelFactory = TodoViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}