package com.a65apps.multiplatform.sample.presentation.archive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.a65apps.multiplatform.domain.Task
import com.a65apps.multiplatform.sample.R

private val diffCallback = object : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
        oldItem == newItem
}

class ArchiveListAdapter(
    private val listener: (id: String) -> Unit
) : RecyclerView.Adapter<ArchiveListAdapter.ArchiveViewHolder>() {

    val differ = AsyncListDiffer<Task>(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewHolder =
        ArchiveViewHolder(
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false),
            listener = listener,
            differ = differ
        )

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArchiveViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun submitList(items: List<Task>) {
        differ.submitList(items)
    }

    class ArchiveViewHolder(
        view: View,
        listener: (id: String) -> Unit,
        differ: AsyncListDiffer<Task>
    ) : RecyclerView.ViewHolder(view), ItemTouchViewHolder {

        private val taskTitle = ViewCompat.requireViewById<TextView>(view, R.id.taskTitle)
        private val taskDescription =
            ViewCompat.requireViewById<TextView>(view, R.id.taskDescription)

        init {
            view.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener(differ.currentList[pos].id)
                }
            }
        }

        override fun onItemSelected() {
            // no op
        }

        override fun onItemCleared() {
            // no op
        }

        fun bind(task: Task) {
            taskTitle.text = task.title
            taskDescription.text = task.description
        }
    }
}
