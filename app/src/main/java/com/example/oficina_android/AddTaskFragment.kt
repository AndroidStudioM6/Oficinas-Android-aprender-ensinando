package com.example.oficina_android

import android.os.Bundle
import android.app.DatePickerDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import java.util.Locale

class AddTaskFragment : Fragment(R.layout.activity_task_create) {

    private val dateFormat: SimpleDateFormat
        get() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { isLenient = false }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TarefaRepositorio.inicializar(requireContext().applicationContext)

        val priorities = listOf(
            getString(R.string.priority_high) to Prioridade.ALTA,
            getString(R.string.priority_medium) to Prioridade.MEDIA,
            getString(R.string.priority_low) to Prioridade.BAIXA
        )
        val priorityInput = view.findViewById<AutoCompleteTextView>(R.id.actvPriority)
        priorityInput.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, priorities.map { it.first })
        )
        priorityInput.setText(priorities.first().first, false)


        val titleInput = view.findViewById<TextInputEditText>(R.id.etTitle)
        val descriptionInput = view.findViewById<TextInputEditText>(R.id.etDescription)
        val categoryInput = view.findViewById<TextInputEditText>(R.id.etCategory)
        val creationDateInput = view.findViewById<TextInputEditText>(R.id.etCreationDate)
        val dueDateInput = view.findViewById<TextInputEditText>(R.id.etDueDate)
        val completedInput = view.findViewById<CheckBox>(R.id.cbCompleted)
        val saveButton = view.findViewById<Button>(R.id.btnSaveTask)

        val creationDateField = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilCreationDate)
        val dueDateField = view.findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.tilDueDate)
        val openCreationDatePicker = { showDatePicker(creationDateInput) }
        val openDueDatePicker = { showDatePicker(dueDateInput) }
        creationDateInput.setOnClickListener { openCreationDatePicker() }
        creationDateField.setEndIconOnClickListener { openCreationDatePicker() }
        dueDateInput.setOnClickListener { openDueDatePicker() }
        dueDateField.setEndIconOnClickListener { openDueDatePicker() }

        titleInput.text?.clear()
        descriptionInput.text?.clear()
        creationDateInput.setText(dateFormat.format(Date()))
        dueDateInput.text?.clear()

        saveButton.setOnClickListener {
            val title = titleInput.text?.toString()?.trim().orEmpty()
            if (title.isEmpty()) {
                titleInput.error = getString(R.string.error_task_title_required)
                titleInput.requestFocus()
                return@setOnClickListener
            }

            val creationDate = parseDate(creationDateInput.text?.toString().orEmpty())
            if (creationDate == null) {
                creationDateInput.error = getString(R.string.error_invalid_date)
                return@setOnClickListener
            }

            val dueDateText = dueDateInput.text?.toString()?.trim().orEmpty()
            val dueDate = if (dueDateText.isEmpty()) null else parseDate(dueDateText)
            if (dueDateText.isNotEmpty() && dueDate == null) {
                dueDateInput.error = getString(R.string.error_invalid_date)
                return@setOnClickListener
            }

            val selectedPriority = priorities.firstOrNull { it.first == priorityInput.text?.toString() }?.second
                ?: Prioridade.MEDIA
            val task = Tarefa(
                titulo = title,
                descricao = descriptionInput.text?.toString()?.trim().orEmpty(),
                categoria = categoryInput.text?.toString()?.trim()
                    .takeUnless { it.isNullOrEmpty() } ?: getString(R.string.category_general),
                prioridade = selectedPriority,
                concluida = completedInput.isChecked,
                dataCriacao = creationDate.time,
                dataEntrega = dueDate?.time
            )

            titleInput.error = null
            saveButton.isEnabled = false
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    TarefaRepositorio.adicionar(task)
                    ViewModelProvider(requireActivity())[TaskViewModel::class.java].refreshTasks()
                    Toast.makeText(requireContext(), R.string.toast_task_created, Toast.LENGTH_SHORT).show()
                    (activity as? MainActivity)?.selectPage(2)
                } catch (error: Exception) {
                    saveButton.isEnabled = true
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun parseDate(value: String): Date? {
        val input = value.trim()
        if (input.isEmpty()) return null
        val position = ParsePosition(0)
        val parsed = dateFormat.parse(input, position)
        return parsed?.takeIf { position.index == input.length }
    }

    private fun showDatePicker(input: TextInputEditText) {
        val initialDate = parseDate(input.text?.toString().orEmpty()) ?: Date()
        val calendar = Calendar.getInstance().apply { time = initialDate }
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                input.setText(dateFormat.format(calendar.time))
                input.error = null
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
