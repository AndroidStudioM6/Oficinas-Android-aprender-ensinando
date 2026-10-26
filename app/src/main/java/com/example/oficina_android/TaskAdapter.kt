package com.example.oficina_android

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onTaskChecked: (Task, Boolean) -> Unit,
    private val onTaskDeleted: (Task) -> Unit,
    private val onTaskClicked: (Task) -> Unit = {}
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbTask: CheckBox = itemView.findViewById(R.id.cbTask)
        val tvTaskTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        val tvTaskDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        val tvTaskCategory: TextView = itemView.findViewById(R.id.tvTaskCategory)
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

        holder.tvTaskTitle.text = task.title
        holder.tvTaskDescription.text = task.description
        holder.tvTaskDescription.visibility = if (task.description.isBlank()) View.GONE else View.VISIBLE
        holder.tvTaskCategory.text = holder.itemView.context.getString(
            R.string.label_task_category_value,
            task.category
        )
        holder.tvPriorityChip.text = task.priority
        PriorityTheme.applyBadge(holder.tvPriorityChip, task.priority)
        holder.tvCreationDate.text = holder.itemView.context.getString(
            R.string.label_created_at,
            task.creationDate
        )
        holder.tvDueDate.text = if (task.dueDate.isBlank()) "" else holder.itemView.context.getString(
            R.string.label_due_at,
            task.dueDate
        )
        holder.tvDueDate.visibility = if (task.dueDate.isBlank()) View.GONE else View.VISIBLE

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

        holder.itemView.setOnClickListener {
            onTaskClicked(task)
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

    fun updateTasks(newTasks: List<Task>) {
        val oldTasks = tasks.toList()
        val updatedTasks = newTasks.toList()
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldTasks.size
            override fun getNewListSize(): Int = updatedTasks.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldTasks[oldItemPosition].id == updatedTasks[newItemPosition].id
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldTasks[oldItemPosition] == updatedTasks[newItemPosition]
        })
        tasks.clear()
        tasks.addAll(updatedTasks)
        diff.dispatchUpdatesTo(this)
    }
}
