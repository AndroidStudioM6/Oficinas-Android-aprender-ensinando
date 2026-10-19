package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText

/**
 * Fragment para a lista de tarefas.
 */
class TaskListFragment : Fragment(R.layout.activity_task_list) {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    private lateinit var fabAddTask: ExtendedFloatingActionButton
    private lateinit var etTaskInput: TextInputEditText
    private lateinit var btnAdd: Button
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        initViews(view)
        setupRecyclerView()
        setupListeners()

        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter = TaskAdapter(
                tasks,
                onTaskChecked = { task, isChecked ->
                    viewModel.updateTaskCompletion(task, isChecked)
                },
                onTaskDeleted = { task ->
                    viewModel.removeTask(task)
                    updateEmptyState(tasks)
                }
            )
            rvTasks.adapter = adapter
            updateEmptyState(tasks)
        }

        arguments?.getBoolean("FOCUS_ADD", false)?.let { focusAdd ->
            if (focusAdd) {
                etTaskInput.requestFocus()
            }
        }
    }

    private fun initViews(view: View) {
        fabAddTask = view.findViewById(R.id.fabAddTask)
        etTaskInput = view.findViewById(R.id.etTaskInput)
        btnAdd = view.findViewById(R.id.btnAdd)
        rvTasks = view.findViewById(R.id.rvTasks)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        val toolbar = view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbarTaskList)
        toolbar?.setNavigationOnClickListener {
            // No action needed for fragment within bottom nav
        }
    }

    private fun setupRecyclerView() {
        val currentTasks = viewModel.tasks.value ?: mutableListOf()
        adapter = TaskAdapter(
            currentTasks,
            onTaskChecked = { task, isChecked ->
                viewModel.updateTaskCompletion(task, isChecked)
            },
            onTaskDeleted = { task ->
                viewModel.removeTask(task)
                updateEmptyState(viewModel.tasks.value ?: mutableListOf())
            }
        )
        rvTasks.adapter = adapter
    }

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            val text = etTaskInput.text?.toString()?.trim() ?: ""
            if (text.isNotEmpty()) {
                val newTask = Task(title = text)
                viewModel.addTask(newTask)
                etTaskInput.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Digite o título da tarefa", Toast.LENGTH_SHORT).show()
            }
        }

        fabAddTask.setOnClickListener {
            findNavController().navigate(R.id.nav_add_task)
        }
    }

    private fun updateEmptyState(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }
}
