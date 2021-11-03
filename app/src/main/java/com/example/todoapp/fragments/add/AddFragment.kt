package com.example.todoapp.fragments.add
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.TodoData
import com.example.todoapp.viewmodels.TodoViewModel
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment(R.layout.fragment_add) {

    lateinit var todoViewModel: TodoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoViewModel = (activity as MainActivity).viewModel

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {

            val title = title_et.text.toString()
            val description = description_et.text.toString()
            val priority = priorities_spinner.selectedItem.toString()

            val validate = verifyDataFromUser(title, description)

            if (validate) {
                val newData = TodoData(0, title, parsePriority(priority), description)
                todoViewModel.insertData(newData)
                Toast.makeText(requireContext(), "Successfully added.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_listFragment)

            } else {
                Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun verifyDataFromUser(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    private fun parsePriority(priority: String): Priority {
        return when(priority) {
            "High Priority" -> Priority.HIGH
            "Medium Priority" -> Priority.MEDIUM
            "Low Priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }
}