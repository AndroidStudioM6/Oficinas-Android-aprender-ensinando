package com.example.oficina_android

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onTaskDeleted: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbTask: CheckBox = itemView.findViewById(R.id.cbTask)
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvTaskDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val tvPriorityChip: TextView = itemView.findViewById(R.id.tvPriorityChip)
        val tvCreationDate: TextView = itemView.findViewById(R.id.tvCreationDate)
        val tvDueDate: TextView = itemView.findViewById(R.id.tvDueDate)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        val context = holder.itemView.context

        holder.tvTaskTitle.text = task.title

        if (task.description.isNotBlank()) {
            holder.tvTaskDescription.text = task.description
            holder.tvTaskDescription.visibility = View.VISIBLE
        } else {
            holder.tvTaskDescription.visibility = View.GONE
        }

        holder.tvPriorityChip.text = task.priority

        if (task.creationDate.isNotBlank()) {
            holder.tvCreationDate.text = context.getString(R.string.label_created_at, task.creationDate)
            holder.tvCreationDate.visibility = View.VISIBLE
        } else {
            holder.tvCreationDate.visibility = View.GONE
        }

        if (task.dueDate.isNotBlank()) {
            holder.tvDueDate.text = context.getString(R.string.label_due_at, task.dueDate)
            holder.tvDueDate.visibility = View.VISIBLE
        } else {
            holder.tvDueDate.visibility = View.GONE
        }

        // Tira o listener antes de mudar o estado, para não disparar em Views recicladas
        holder.cbTask.setOnCheckedChangeListener(null)
        holder.cbTask.isChecked = task.isCompleted
        updateStrikethrough(holder.tvTaskTitle, task.isCompleted)

        holder.cbTask.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            updateStrikethrough(holder.tvTaskTitle, isChecked)
            onTaskChecked(task, isChecked)
        }

        holder.btnDelete.setOnClickListener {
            onTaskDeleted(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    private fun updateStrikethrough(textView: TextView, isCompleted: Boolean) {
        textView.paintFlags = if (isCompleted) {
            textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }

    fun removeTask(task: Task) {
        val index = tasks.indexOf(task)
        if (index != -1) {
            tasks.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
