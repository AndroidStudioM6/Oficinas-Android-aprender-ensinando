package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment para o cadastro completo de tarefas.
 */
class AddTaskFragment : Fragment(R.layout.activity_task_create) {

    private lateinit var viewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbarTaskCreate)
        toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        val priorityOptions = arrayOf(
            getString(R.string.priority_high),
            getString(R.string.priority_medium),
            getString(R.string.priority_low)
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, priorityOptions)
        val actvPriority = view.findViewById<AutoCompleteTextView>(R.id.actvPriority)
        actvPriority.setAdapter(adapter)

        val etTitle = view.findViewById<TextInputEditText>(R.id.etTitle)
        val etDescription = view.findViewById<TextInputEditText>(R.id.etDescription)
        val etCreationDate = view.findViewById<TextInputEditText>(R.id.etCreationDate)
        val etDueDate = view.findViewById<TextInputEditText>(R.id.etDueDate)
        val cbCompleted = view.findViewById<CheckBox>(R.id.cbCompleted)

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        if (etCreationDate.text.isNullOrBlank()) {
            etCreationDate.setText(currentDate)
        }

        val btnSaveTask = view.findViewById<Button>(R.id.btnSaveTask)
        val btnCancelTask = view.findViewById<Button>(R.id.btnCancelTask)

        btnSaveTask.setOnClickListener {
            val title = etTitle.text?.toString()?.trim() ?: ""
            if (title.isEmpty()) {
                etTitle.error = "Digite o título da tarefa"
                Toast.makeText(requireContext(), "Por favor, informe o título da tarefa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val description = etDescription.text?.toString()?.trim() ?: ""
            val priority = actvPriority.text?.toString()?.takeIf { it.isNotBlank() } ?: "Média"
            val creationDate = etCreationDate.text?.toString()?.trim() ?: ""
            val dueDate = etDueDate.text?.toString()?.trim() ?: ""
            val isCompleted = cbCompleted.isChecked

            val newTask = Task(
                title = title,
                description = description,
                priority = priority,
                creationDate = creationDate,
                dueDate = dueDate,
                isCompleted = isCompleted
            )

            viewModel.addTask(newTask)
            Toast.makeText(requireContext(), getString(R.string.toast_task_created), Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.nav_task_list)
        }

        btnCancelTask.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
