package com.a65apps.multiplatform.sample.presentation.todo

import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.domain.TaskStatus
import com.a65apps.multiplatform.sample.R

private val diffCallback = object : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem == newItem
}

internal class TodoListAdapter(
    private val listener: (id: String) -> Unit
) : RecyclerView.Adapter<TodoListAdapter.TodoViewHolder>() {

    private val differ = AsyncListDiffer<Task>(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder =
        TodoViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false),
            listener = listener,
            differ = differ
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun submitList(items: List<Task>) {
        differ.submitList(items)
    }

    class TodoViewHolder(
        view: View,
        listener: (id: String) -> Unit,
        differ: AsyncListDiffer<Task>
    ) : RecyclerView.ViewHolder(view) {

        private val taskTitle = ViewCompat.requireViewById<TextView>(view, R.id.taskTitle)
        private val taskDescription = ViewCompat.requireViewById<TextView>(view, R.id.taskDescription)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener(differ.currentList[pos].id)
                }
            }
        }

        fun bind(task: Task) {
            val title = SpannableString(task.title)
            val description = SpannableString(task.description)
            if (task.status == TaskStatus.DONE) {
                title.setSpan(StrikethroughSpan(), 0, title.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                description.setSpan(StrikethroughSpan(), 0, description.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            taskTitle.text = title
            taskDescription.text = description
        }
    }
}
