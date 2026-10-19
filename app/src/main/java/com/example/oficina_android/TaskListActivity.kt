package com.example.oficina_android

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

class TaskListActivity : AppCompatActivity() {

    private val taskList = mutableListOf(
        Task(title = "Estudar para a prova"),
        Task(title = "Fazer os exercícios de Android"),
        Task(title = "Comprar pão", isCompleted = true)
    )

    private lateinit var adapter: TaskAdapter
    private lateinit var etTaskInput: TextInputEditText
    private lateinit var btnAdd: Button
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)

        val rootLayout = findViewById<View>(R.id.mainTaskList)
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        initViews()
        setupRecyclerView()
        setupListeners()
        updateEmptyState()

        if (intent.getBooleanExtra("FOCUS_ADD", false)) {
            etTaskInput.requestFocus()
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        etTaskInput = findViewById(R.id.etTaskInput)
        btnAdd = findViewById(R.id.btnAdd)
        rvTasks = findViewById(R.id.rvTasks)
        tvEmpty = findViewById(R.id.tvEmpty)
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            taskList,
            onTaskChecked = { _, _ -> },
            onTaskDeleted = { task ->
                adapter.removeTask(task)
                updateEmptyState()
            }
        )
        rvTasks.adapter = adapter
    }

    private fun setupListeners() {
        btnAdd.setOnClickListener {
            val text = etTaskInput.text?.toString()?.trim() ?: ""
            if (text.isNotEmpty()) {
                val newTask = Task(title = text)
                adapter.addTask(newTask)
                etTaskInput.text?.clear()
                updateEmptyState()
            } else {
                Toast.makeText(this, "Digite o título da tarefa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateEmptyState() {
        if (taskList.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            rvTasks.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            rvTasks.visibility = View.VISIBLE
        }
    }
}
