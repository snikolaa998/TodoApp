package com.example.todoapp.fragments.list
import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.adapters.TodoListAdapter
import com.example.todoapp.viewmodels.TodoViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(R.layout.fragment_list) {

    lateinit var todoListAdapter: TodoListAdapter
    lateinit var todoViewModel: TodoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        setupRecyclerView()
        todoViewModel = (activity as MainActivity).viewModel

        floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }

        todoViewModel.getAllData().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                showNoData()
            } else {
                hideNoData()
            }
            todoListAdapter.todos = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            deleteAllData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        todoListAdapter = TodoListAdapter()
        recyclerView.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun deleteAllData() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            todoViewModel.deleteAll()
        }
        builder.setNegativeButton("No") {_, _ ->
            //Do nothing
        }
        builder.setTitle("Delete all todo items")
        builder.setMessage("Are you sure you want to remove all items?")
        builder.create().show()
    }

    private fun showNoData() {
        no_data_imageView.visibility = View.VISIBLE
        no_data_textView.visibility = View.VISIBLE
    }

    private fun hideNoData() {
        no_data_imageView.visibility = View.INVISIBLE
        no_data_textView.visibility = View.INVISIBLE
    }
}