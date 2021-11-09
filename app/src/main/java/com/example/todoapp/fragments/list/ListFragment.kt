package com.example.todoapp.fragments.list
import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.todoapp.MainActivity
import com.example.todoapp.R
import com.example.todoapp.adapters.SwipeToDelete
import com.example.todoapp.adapters.TodoListAdapter
import com.example.todoapp.viewmodels.TodoViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {

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
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> deleteAllData()
            R.id.menu_priority_high -> todoViewModel.getHighPriority().observe(viewLifecycleOwner) {
                todoListAdapter.todos = it
            }
            R.id.menu_priority_low -> todoViewModel.getLowPriority().observe(viewLifecycleOwner) {
                todoListAdapter.todos = it
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        var searchQuery = query
        searchQuery = "%$searchQuery%"

        todoViewModel.searchDatabase(searchQuery).observe(this) {
            it?.let {
               todoListAdapter.todos = it
            }
        }
    }

    private fun setupRecyclerView() {
        todoListAdapter = TodoListAdapter()
        recyclerView.apply {
            adapter = todoListAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            itemAnimator = SlideInUpAnimator().apply {
                addDuration = 300
            }
        }
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val todoItem = todoListAdapter.todos[position]
                todoViewModel.deleteData(todoItem)
                Snackbar.make(view!!, "Successfully deleted.", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        todoViewModel.insertData(todoItem)
                    }
                    show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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