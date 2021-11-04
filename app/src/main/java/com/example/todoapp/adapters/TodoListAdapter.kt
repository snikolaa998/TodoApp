package com.example.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.TodoData
import kotlinx.android.synthetic.main.row_item.view.*

class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {

    var dataList = emptyList<TodoData>()

    private val diffCallBack = object : DiffUtil.ItemCallback<TodoData>() {
        override fun areItemsTheSame(oldItem: TodoData, newItem: TodoData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TodoData, newItem: TodoData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    var todos: List<TodoData>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val todo = todos[position]
        val priority = todo.priority

        holder.itemView.apply {
            item_title.text = todo.title
            item_description.text = todo.description
            when(priority) {
                Priority.HIGH -> priority_indicator.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red))
                Priority.MEDIUM -> priority_indicator.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                Priority.LOW -> priority_indicator.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    fun setData(todoData: List<TodoData>) {
        this.dataList = todoData
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}