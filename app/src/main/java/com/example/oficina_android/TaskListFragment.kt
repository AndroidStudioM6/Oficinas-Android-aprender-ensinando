package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var searchInput: TextInputEditText
    private lateinit var statusFilters: ChipGroup
    private lateinit var clearCompletedButton: MaterialButton
    private lateinit var clearAllButton: MaterialButton
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
        clearCompletedButton = view.findViewById(R.id.btnClearCompleted)
        clearAllButton = view.findViewById(R.id.btnClearAll)
        priorityFilter = view.findViewById(R.id.priorityFilter)
        categoryFilter = view.findViewById(R.id.categoryFilter)

        adapter = TaskAdapter(
            tasks = mutableListOf(),
            onTaskChecked = { task, isChecked -> viewModel.toggleTaskCompleted(task, isChecked) },
            onTaskDeleted = { task -> viewModel.removeTask(task) },
            onTaskClicked = { task ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val storedTask = TarefaRepositorio.listarTodas()
                        .firstOrNull { it.id.toLong() == task.id }
                    if (storedTask != null && isAdded) {
                        TaskEditDialog(requireContext(), viewLifecycleOwner.lifecycleScope)
                            .show(storedTask) { viewModel.refreshTasks() }
                    }
                }
            }
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
            getString(R.string.priority_low),
            getString(R.string.priority_medium),
            getString(R.string.priority_high)
        )
        priorityFilter.setAdapter(StaticDropdownAdapter(requireContext(), priorities))
        priorityFilter.setText(priorities.first(), false)
        PriorityTheme.applyText(priorityFilter, priorities.first())
        priorityFilter.setOnClickListener { priorityFilter.showDropDown() }

        statusFilters.setOnCheckedStateChangeListener { _, _ -> applyFilters() }
        clearCompletedButton.setOnClickListener { confirmClearCompletedTasks() }
        clearAllButton.setOnClickListener { confirmClearAllTasks() }
        searchInput.addTextChangedListener(AfterTextChangedListener { applyFilters() })
        priorityFilter.setOnItemClickListener { _, _, position, _ ->
            PriorityTheme.applyText(priorityFilter, priorities[position])
            applyFilters()
        }
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
        clearCompletedButton.isEnabled = allTasks.any { it.isCompleted }
        clearAllButton.isEnabled = allTasks.isNotEmpty()
        val noTasks = filtered.isEmpty()
        tvEmpty.text = getString(
            if (allTasks.isEmpty()) R.string.empty_tasks else R.string.filter_no_results
        )
        tvEmpty.visibility = if (noTasks) View.VISIBLE else View.GONE
        rvTasks.visibility = if (noTasks) View.GONE else View.VISIBLE
    }

    private fun confirmClearCompletedTasks() {
        if (allTasks.none { it.isCompleted }) return

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_clear_completed)
            .setMessage(R.string.message_clear_completed)
            .setNegativeButton(R.string.btn_cancel, null)
            .setPositiveButton(R.string.btn_clear) { _, _ -> viewModel.clearCompletedTasks() }
            .show()
    }

    private fun confirmClearAllTasks() {
        if (allTasks.isEmpty()) return

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_clear_all_tasks)
            .setMessage(R.string.message_clear_all_tasks)
            .setNegativeButton(R.string.btn_cancel, null)
            .setPositiveButton(R.string.btn_delete) { _, _ -> viewModel.clearAllTasks() }
            .show()
    }

    private class AfterTextChangedListener(private val action: () -> Unit) : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: android.text.Editable?) = action()
    }
}
