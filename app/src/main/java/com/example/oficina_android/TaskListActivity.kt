package com.example.oficina_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class TaskListActivity : AppCompatActivity() {

    private val taskList = mutableListOf(
        Task(
            title = "Estudar para a prova de Android",
            description = "Revisar Activity, Layouts XML, Eventos e Material Design 3.",
            priority = "Alta",
            creationDate = "15/10/2026",
            dueDate = "20/10/2026",
            isCompleted = false
        ),
        Task(
            title = "Implementar interface de cadastro de tarefas",
            description = "Criar os campos de título, descrição, prioridade, prazos e checkbox.",
            priority = "Alta",
            creationDate = "16/10/2026",
            dueDate = "22/10/2026",
            isCompleted = false
        ),
        Task(
            title = "Criar tela Sobre o Projeto",
            description = "Apresentar descrição do app, funcionalidades e equipe.",
            priority = "Média",
            creationDate = "17/10/2026",
            dueDate = "25/10/2026",
            isCompleted = true
        )
    )

    private lateinit var adapter: TaskAdapter
    private lateinit var fabAddTask: ExtendedFloatingActionButton
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainTaskList)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupRecyclerView()
        setupListeners()
        updateEmptyState()
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbarTaskList)
        fabAddTask = findViewById(R.id.fabAddTask)
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
        toolbar.setNavigationOnClickListener {
            finish()
        }

        fabAddTask.setOnClickListener {
            val intent = Intent(this, TaskCreateActivity::class.java)
            startActivity(intent)
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
