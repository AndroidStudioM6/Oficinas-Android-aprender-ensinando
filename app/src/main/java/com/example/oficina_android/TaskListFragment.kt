package com.example.oficina_android

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
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
        observeViewModel()

        parentFragmentManager.setFragmentResultListener("TASK_LIST_FOCUS", viewLifecycleOwner) { _, bundle ->
            val shouldFocus = bundle.getBoolean("FOCUS_INPUT", false)
            if (shouldFocus) {
                focusInput()
            } else {
                clearInputFocus()
            }
        }
    }

    private fun initViews(view: View) {
        etTaskInput = view.findViewById(R.id.etTaskInput)
        btnAdd = view.findViewById(R.id.btnAdd)
        rvTasks = view.findViewById(R.id.rvTasks)
        tvEmpty = view.findViewById(R.id.tvEmpty)
    }

    fun focusInput() {
        if (::etTaskInput.isInitialized) {
            etTaskInput.post {
                etTaskInput.requestFocus()
                hideKeyboard()
            }
        }
    }

    fun clearInputFocus() {
        if (::etTaskInput.isInitialized) {
            etTaskInput.post {
                etTaskInput.clearFocus()
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(etTaskInput.windowToken, 0)
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            tasks = mutableListOf(),
            onTaskChecked = { task, isChecked ->
                viewModel.toggleTaskCompleted(task, isChecked)
            },
            onTaskDeleted = { task ->
                viewModel.removeTask(task)
            }
        )
        rvTasks.adapter = adapter
    }

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            val text = etTaskInput.text?.toString()?.trim() ?: ""
            if (text.isNotEmpty()) {
                viewModel.addTask(text)
                etTaskInput.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Digite o título da tarefa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            adapter.updateTasks(tasks)
            updateEmptyState(tasks)
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
