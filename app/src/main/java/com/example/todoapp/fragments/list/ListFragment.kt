package com.example.todoapp.fragments.list
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
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
            todoListAdapter.todos = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    private fun setupRecyclerView() {
        todoListAdapter = TodoListAdapter()
        recyclerView.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}