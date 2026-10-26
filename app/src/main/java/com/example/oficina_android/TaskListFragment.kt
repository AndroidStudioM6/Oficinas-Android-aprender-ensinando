package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var searchInput: TextInputEditText
    private lateinit var statusFilters: ChipGroup
    private lateinit var priorityFilter: AutoCompleteTextView
    private lateinit var categoryFilter: AutoCompleteTextView
    private var allTasks: List<Task> = emptyList()

    override fun onResume() {
        super.onResume()
        if (::viewModel.isInitialized) viewModel.refreshTasks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
        rvTasks = view.findViewById(R.id.rvTasks)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        searchInput = view.findViewById(R.id.etSearchTasks)
        statusFilters = view.findViewById(R.id.statusFilters)
        priorityFilter = view.findViewById(R.id.priorityFilter)
        categoryFilter = view.findViewById(R.id.categoryFilter)

        adapter = TaskAdapter(
            tasks = mutableListOf(),
            onTaskChecked = { task, isChecked -> viewModel.toggleTaskCompleted(task, isChecked) },
            onTaskDeleted = { task -> viewModel.removeTask(task) }
        )
        rvTasks.adapter = adapter

        setupFilters()

        viewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            allTasks = tasks
            updateCategoryOptions(tasks)
            applyFilters()
        }
    }

    private fun setupFilters() {
        statusFilters.check(R.id.filterAll)

        val priorities = listOf(
            getString(R.string.filter_all),
            getString(R.string.priority_high),
            getString(R.string.priority_medium),
            getString(R.string.priority_low)
        )
        priorityFilter.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, priorities)
        )
        priorityFilter.setText(priorities.first(), false)

        statusFilters.setOnCheckedStateChangeListener { _, _ -> applyFilters() }
        searchInput.addTextChangedListener(AfterTextChangedListener { applyFilters() })
        priorityFilter.setOnItemClickListener { _, _, _, _ -> applyFilters() }
        categoryFilter.setOnItemClickListener { _, _, _, _ -> applyFilters() }
    }

    private fun updateCategoryOptions(tasks: List<Task>) {
        val selectedCategory = categoryFilter.text?.toString().orEmpty()
        val categories = listOf(getString(R.string.filter_all)) + tasks
            .map { it.category }
            .distinct()
            .sorted()
        categoryFilter.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        )
        categoryFilter.setText(
            selectedCategory.takeIf { it in categories } ?: categories.first(),
            false
        )
    }

    private fun applyFilters() {
        val query = searchInput.text?.toString()?.trim().orEmpty()
        val selectedPriority = priorityFilter.text?.toString().orEmpty()
        val selectedCategory = categoryFilter.text?.toString().orEmpty()
        val statusId = statusFilters.checkedChipId

        val filtered = allTasks.filter { task ->
            val matchesQuery = query.isEmpty() || task.title.contains(query, true) ||
                task.description.contains(query, true)
            val matchesStatus = when (statusId) {
                R.id.filterPending -> !task.isCompleted
                R.id.filterCompleted -> task.isCompleted
                else -> true
            }
            val matchesPriority = selectedPriority.isBlank() ||
                selectedPriority == getString(R.string.filter_all) ||
                task.priority.equals(selectedPriority, ignoreCase = true)
            val matchesCategory = selectedCategory.isBlank() ||
                selectedCategory == getString(R.string.filter_all) ||
                task.category.equals(selectedCategory, ignoreCase = true)

            matchesQuery && matchesStatus && matchesPriority && matchesCategory
        }

        adapter.updateTasks(filtered)
        val noTasks = filtered.isEmpty()
        tvEmpty.text = getString(
            if (allTasks.isEmpty()) R.string.empty_tasks else R.string.filter_no_results
        )
        tvEmpty.visibility = if (noTasks) View.VISIBLE else View.GONE
        rvTasks.visibility = if (noTasks) View.GONE else View.VISIBLE
    }

    private class AfterTextChangedListener(private val action: () -> Unit) : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: android.text.Editable?) = action()
    }
}
