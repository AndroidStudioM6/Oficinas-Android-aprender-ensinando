package com.example.oficina_android

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskEditDialog(
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope
) {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        isLenient = false
    }

    fun show(task: Tarefa, onSaved: () -> Unit) {
        val form = LayoutInflater.from(context).inflate(R.layout.activity_task_create, null)
        form.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val titleInput = form.findViewById<TextInputEditText>(R.id.etTitle)
        val descriptionInput = form.findViewById<TextInputEditText>(R.id.etDescription)
        val categoryInput = form.findViewById<TextInputEditText>(R.id.etCategory)
        val priorityInput = form.findViewById<AutoCompleteTextView>(R.id.actvPriority)
        val creationDateInput = form.findViewById<TextInputEditText>(R.id.etCreationDate)
        val dueDateInput = form.findViewById<TextInputEditText>(R.id.etDueDate)
        val completedInput = form.findViewById<CheckBox>(R.id.cbCompleted)
        val embeddedSaveButton = form.findViewById<Button>(R.id.btnSaveTask)

        val priorities = listOf(
            context.getString(R.string.priority_low) to Prioridade.BAIXA,
            context.getString(R.string.priority_medium) to Prioridade.MEDIA,
            context.getString(R.string.priority_high) to Prioridade.ALTA
        )
        priorityInput.setAdapter(StaticDropdownAdapter(context, priorities.map { it.first }))
        val selectedPriority = priorities.first { it.second == task.prioridade }
        priorityInput.setText(selectedPriority.first, false)
        PriorityTheme.applyText(priorityInput, selectedPriority.first)
        priorityInput.setOnItemClickListener { _, _, position, _ ->
            PriorityTheme.applyText(priorityInput, priorities[position].first)
        }
        priorityInput.setOnClickListener { priorityInput.showDropDown() }

        titleInput.setText(task.titulo)
        descriptionInput.setText(task.descricao)
        categoryInput.setText(task.categoria)
        creationDateInput.setText(dateFormat.format(Date(task.dataCriacao)))
        dueDateInput.setText(task.dataEntrega?.let { dateFormat.format(Date(it)) }.orEmpty())
        completedInput.isChecked = task.concluida
        embeddedSaveButton.visibility = View.GONE
        DateInputFormatter.attach(creationDateInput)
        DateInputFormatter.attach(dueDateInput)

        val creationDateField = form.findViewById<TextInputLayout>(R.id.tilCreationDate)
        val dueDateField = form.findViewById<TextInputLayout>(R.id.tilDueDate)
        val showCreationDatePicker = { showDatePicker(creationDateInput) }
        val showDueDatePicker = { showDatePicker(dueDateInput) }
        creationDateField.setEndIconOnClickListener { showCreationDatePicker() }
        dueDateField.setEndIconOnClickListener { showDueDatePicker() }

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.edit_task_title)
            .setView(form)
            .setNegativeButton(R.string.btn_cancel, null)
            .setPositiveButton(R.string.btn_save_changes, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val title = titleInput.text?.toString()?.trim().orEmpty()
                if (title.isEmpty()) {
                    titleInput.error = context.getString(R.string.error_task_title_required)
                    titleInput.requestFocus()
                    return@setOnClickListener
                }

                val creationDate = parseDate(creationDateInput.text?.toString().orEmpty())
                if (creationDate == null) {
                    creationDateInput.error = context.getString(R.string.error_invalid_date)
                    return@setOnClickListener
                }
                val dueDateText = dueDateInput.text?.toString()?.trim().orEmpty()
                val dueDate = if (dueDateText.isEmpty()) null else parseDate(dueDateText)
                if (dueDateText.isNotEmpty() && dueDate == null) {
                    dueDateInput.error = context.getString(R.string.error_invalid_date)
                    return@setOnClickListener
                }
                if (dueDate != null && creationDate.after(dueDate)) {
                    dueDateInput.error = context.getString(R.string.error_due_date_before_creation)
                    dueDateInput.requestFocus()
                    return@setOnClickListener
                }
                creationDateInput.error = null
                dueDateInput.error = null

                val updatedTask = task.copy(
                    titulo = title,
                    descricao = descriptionInput.text?.toString()?.trim().orEmpty(),
                    categoria = categoryInput.text?.toString()?.trim()
                        .takeUnless { it.isNullOrEmpty() } ?: context.getString(R.string.category_general),
                    prioridade = priorities.firstOrNull { it.first == priorityInput.text?.toString() }?.second
                        ?: Prioridade.MEDIA,
                    concluida = completedInput.isChecked,
                    dataCriacao = creationDate.time,
                    dataEntrega = dueDate?.time
                )

                val saveButton = dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                saveButton.isEnabled = false
                lifecycleScope.launch {
                    try {
                        TarefaRepositorio.atualizar(updatedTask)
                        onSaved()
                        dialog.dismiss()
                        Toast.makeText(context, R.string.toast_task_updated, Toast.LENGTH_SHORT).show()
                    } catch (error: Exception) {
                        saveButton.isEnabled = true
                        Toast.makeText(
                            context,
                            error.localizedMessage ?: context.getString(R.string.error_task_update),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        dialog.show()
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
            context,
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
