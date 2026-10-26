package com.example.oficina_android

import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskCreateActivity : AppCompatActivity() {

    private val dateFormat: SimpleDateFormat
        get() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply { isLenient = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_create)
        TarefaRepositorio.inicializar(applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainTaskCreate)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val priorityOptions = arrayOf(
            getString(R.string.priority_high),
            getString(R.string.priority_medium),
            getString(R.string.priority_low)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, priorityOptions)
        val actvPriority = findViewById<AutoCompleteTextView>(R.id.actvPriority)
        actvPriority.setAdapter(adapter)
        actvPriority.setText(priorityOptions.first(), false)

        val etTitle = findViewById<TextInputEditText>(R.id.etTitle)
        val etDescription = findViewById<TextInputEditText>(R.id.etDescription)
        val etCategory = findViewById<TextInputEditText>(R.id.etCategory)
        val etCreationDate = findViewById<TextInputEditText>(R.id.etCreationDate)
        val etDueDate = findViewById<TextInputEditText>(R.id.etDueDate)
        val cbCompleted = findViewById<CheckBox>(R.id.cbCompleted)
        etTitle.text?.clear()
        etDescription.text?.clear()
        etCreationDate.setText(dateFormat.format(Date()))
        etDueDate.text?.clear()

        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)

        btnSaveTask.setOnClickListener {
            val title = etTitle.text?.toString()?.trim().orEmpty()
            if (title.isEmpty()) {
                etTitle.error = getString(R.string.error_task_title_required)
                etTitle.requestFocus()
                return@setOnClickListener
            }

            val creationDate = parseDate(etCreationDate.text?.toString().orEmpty())
            if (creationDate == null) {
                etCreationDate.error = getString(R.string.error_invalid_date)
                return@setOnClickListener
            }

            val dueDateText = etDueDate.text?.toString()?.trim().orEmpty()
            val dueDate = if (dueDateText.isEmpty()) null else parseDate(dueDateText)
            if (dueDateText.isNotEmpty() && dueDate == null) {
                etDueDate.error = getString(R.string.error_invalid_date)
                return@setOnClickListener
            }

            etTitle.error = null
            val priority = when (actvPriority.text?.toString()) {
                getString(R.string.priority_high) -> Prioridade.ALTA
                getString(R.string.priority_low) -> Prioridade.BAIXA
                else -> Prioridade.MEDIA
            }
            val task = Tarefa(
                titulo = title,
                descricao = etDescription.text?.toString()?.trim().orEmpty(),
                categoria = etCategory.text?.toString()?.trim()
                    .takeUnless { it.isNullOrEmpty() } ?: getString(R.string.category_general),
                prioridade = priority,
                concluida = cbCompleted.isChecked,
                dataCriacao = creationDate.time,
                dataEntrega = dueDate?.time
            )

            btnSaveTask.isEnabled = false
            lifecycleScope.launch {
                try {
                    TarefaRepositorio.adicionar(task)
                    Toast.makeText(this@TaskCreateActivity, R.string.toast_task_created, Toast.LENGTH_SHORT).show()
                    finish()
                } catch (error: Exception) {
                    btnSaveTask.isEnabled = true
                    Toast.makeText(this@TaskCreateActivity, error.localizedMessage, Toast.LENGTH_LONG).show()
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
}
