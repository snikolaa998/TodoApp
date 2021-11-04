package com.example.todoapp.fragments.update
import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.TodoData
import com.example.todoapp.viewmodels.TodoViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_update.*

class UpdateFragment : Fragment(R.layout.fragment_update) {

    private val args: UpdateFragmentArgs by navArgs()
    private lateinit var todoViewModel: TodoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        todoViewModel = (activity as MainActivity).viewModel

        val selectedTodo = args.selectedItem
        current_title_et.setText(selectedTodo.title)
        current_description_et.setText(selectedTodo.description)
        current_priorities_spinner.setSelection(todoViewModel.parsePriorityToInt(selectedTodo.priority))

        current_priorities_spinner.onItemSelectedListener = todoViewModel.listener

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            todoViewModel.deleteData(args.selectedItem)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_, _ ->
            //Do nothing
        }
        builder.setTitle("Delete ${args.selectedItem.title}")
        builder.setMessage("Are you sure you want to remove ${args.selectedItem.title}")
        builder.create().show()
    }

    private fun updateItem() {
        val title = current_title_et.text.toString()
        val description = current_description_et.text.toString()
        val priority = current_priorities_spinner.selectedItem.toString()

        val validation = todoViewModel.verifyDataFromUser(title, description)
        if (validation) {
            val updatedItem = TodoData(args.selectedItem.id, title, todoViewModel.parsePriority(priority), description)
            todoViewModel.updateData(updatedItem)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

}